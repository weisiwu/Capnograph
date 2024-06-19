#include "devicehandler.h"
#include "deviceinfo.h"
#include <QtEndian>
#include <QRandomGenerator>
#include <QtDebug>

#include <QtCharts/QXYSeries>
#include <QtCharts/QAreaSeries>
#include <QtQuick/QQuickView>
#include <QtQuick/QQuickItem>
#include <QtCore/QDebug>
#include <QtCore/QRandomGenerator>
#include <QtCore/QtMath>

QT_CHARTS_USE_NAMESPACE

Q_DECLARE_METATYPE(QAbstractSeries *)
Q_DECLARE_METATYPE(QAbstractAxis *)

DeviceHandler::DeviceHandler(QObject *parent) :
    BluetoothBaseClass(parent),
    m_control(0),
    m_service(0),
    m_renameServer(0),
    m_sendDataServer(0),
    m_currentDevice(0),
    m_antiHijackServer(0),
    m_connectSuccess(false),
    m_foundCO2Service(false),
    m_measuring(false),
    m_Breathe(false),
    m_updateParams(false),
    m_currentCO2(0),
    m_misCount(0),
    m_lastNum(0),
    m_errorRate(0),
    m_ETCO2(0),
    m_etco2UpperFrom(25),
    m_etco2UpperTo(50),
    m_etco2UpperStep(1),
    m_etco2LowerFrom(0),
    m_etco2LowerTo(25),
    m_etco2LowerStep(1),
    m_ETCO2Upper(30),
    m_ETCO2Lower(10),
    m_RRUpper(30),
    m_RRLower(5),
    m_ETCO2Alarm(false),
    m_RRAlarm(false),
    m_RespiratoryRate(0),
    m_pointsBuffFull(false),
    m_sCO2Unit("mmHg"),
    m_time("20S"),
    m_decimal(0),
    m_CO2Scale(50),
    m_wfSpeed(1000),
    m_CO2UnitIndex(0),
    m_CO2ScaleIndex(0),
    m_wfSpeedIndex(1),
    m_barometricPressure(760),
    m_NoBreaths(20),
    m_O2Compensation(16),
    m_index(0),
    m_asphyxialAlarm(false),
    m_noAdapterAlarm(false),
    m_canZero(false),
    cursorWidth(18)

{
    qRegisterMetaType<QAbstractSeries*>();
    qRegisterMetaType<QAbstractAxis*>();

    points.reserve(m_wfSpeed);
    cursorPoints.reserve(cursorWidth);

    getScreenWakeParam();

    m_co2UnitmmHgscaleMode<<50<<60<<75;
    m_co2UnitkpascaleMode<<6.7<<8<<10;
    m_co2UnitpercentscaleMode<<6.6<<7.9<<9.9;
    m_co2ScaleMode<<m_co2UnitmmHgscaleMode<<m_co2UnitkpascaleMode<<m_co2UnitpercentscaleMode;
    m_co2UnitMode<<"mmHg"<<"KPa"<<"%";
    m_co2wfSpeedMode<<"4mm/s"<<"2mm/s"<<"1mm/s";
}

void DeviceHandler::setAddressType(AddressType type)
{
    switch (type) {
    case DeviceHandler::AddressType::PublicAddress:
        m_addressType = QLowEnergyController::PublicAddress;
        break;
    case DeviceHandler::AddressType::RandomAddress:
        m_addressType = QLowEnergyController::RandomAddress;
        break;
    }
}

DeviceHandler::AddressType DeviceHandler::addressType() const
{
    if (m_addressType == QLowEnergyController::RandomAddress)
        return DeviceHandler::AddressType::RandomAddress;

    return DeviceHandler::AddressType::PublicAddress;
}

QString DeviceHandler::currentDeviceName()
{
    return m_currentDeviceName;
}

bool DeviceHandler::connectSuccess()
{
    return m_connectSuccess;
}

void DeviceHandler::setDevice(DeviceInfo *device)
{
    clearMessages();
    startMeasurement();
    m_currentDevice = device;


    // Disconnect and delete old connection
    if (m_control) {
        m_control->disconnectFromDevice();
        delete m_control;
        m_control = 0;
    }

    // Create new controller and connect it if device available
    if (m_currentDevice) {
        m_currentDeviceName=m_currentDevice->getName();
        // Make connections
        //! [Connect-Signals-1]
        m_control = new QLowEnergyController(m_currentDevice->getDevice(), this);
        //! [Connect-Signals-1]
        m_control->setRemoteAddressType(m_addressType);
        //! [Connect-Signals-2]
        connect(m_control, &QLowEnergyController::serviceDiscovered,
                this, &DeviceHandler::serviceDiscovered);
        connect(m_control, &QLowEnergyController::discoveryFinished,
                this, &DeviceHandler::serviceScanDone);

        connect(m_control, static_cast<void (QLowEnergyController::*)(QLowEnergyController::Error)>(&QLowEnergyController::error),
                this, [this](QLowEnergyController::Error error) {
            Q_UNUSED(error);
            //setError("Cannot connect to remote device.");
        });
        connect(m_control, &QLowEnergyController::connected, this, [this]() {
            setInfo("Controller connected. Search services...");
            m_connectSuccess=true;
            emit connectChanged();
            m_control->discoverServices();
        });
        connect(m_control, &QLowEnergyController::disconnected, this, [this]() {
            //setError("LowEnergy controller disconnected");
            setError(languageContent.sDisconnectErr());
            m_connectSuccess=false;
            emit connectChanged();
        });

        // Connect
        m_control->connectToDevice();
        //! [Connect-Signals-2]
    }
}
void DeviceHandler::startMeasurement()
{
    //if (alive()) {
    m_updateParams=false;
    m_foundCO2Service=false;
    m_connectSuccess=false;
    m_measuring = true;
    m_pointsBuffFull=false;

    m_Breathe=false;
    m_ETCO2Alarm=false;
    m_RRAlarm=false;
    m_noAdapterAlarm=false;
    m_asphyxialAlarm=false;

    points.clear();
    points.reserve(m_wfSpeed);
    cursorPoints.clear();
    cursorPoints.reserve(cursorWidth);
    m_index=0;
    m_receiveBuff.clear();
    m_currentDeviceName="";

    emit displayParamChanged();
    emit measuringChanged();
    emit serverFoundChanged();
    emit connectChanged();
    emit alarmChanged();
    //}
}

void DeviceHandler::stopMeasurement()
{
    m_measuring = false;
    emit measuringChanged();
}

void DeviceHandler::serviceDiscovered(const QBluetoothUuid &gatt)
{
    if (gatt == QBluetoothUuid((quint32)BLEServerUUID::BLEReceiveDataSer)) {
        setInfo("CO2 service discovered. Waiting for service scan to be done...");
        m_foundCO2Service = true;
    }
}

void DeviceHandler::serviceScanDone()
{
    setInfo("Service scan done.");

    // Delete old service if available
    if (m_service) {
        delete m_service;
        m_service = 0;
    }

    // If Service found, create new service
    if (m_foundCO2Service){
        m_service = m_control->createServiceObject(QBluetoothUuid((quint32)BLEServerUUID::BLEReceiveDataSer), this);
        emit serverFoundChanged();
    }

    if (m_service) {
        connect(m_service, &QLowEnergyService::stateChanged, this, &DeviceHandler::serviceStateChanged);
        connect(m_service, &QLowEnergyService::characteristicChanged, this, &DeviceHandler::updateCO2Value);
        connect(m_service, &QLowEnergyService::descriptorWritten, this, &DeviceHandler::confirmedDescriptorWrite);
        m_service->discoverDetails();
    } else {
        setError(languageContent.sCO2ServerNotFoundErr());
        emit hideMessage();
    }

    //蓝牙数据
    m_sendDataServer=m_control->createServiceObject(QBluetoothUuid((quint32)BLEServerUUID::BLESendDataSer), this);
    if(m_sendDataServer){
        m_sendDataServer->discoverDetails();
    }

    //模块参数
    m_renameServer=m_control->createServiceObject(QBluetoothUuid((quint32)BLEServerUUID::BLEModuleParamsSer), this);
    if (m_renameServer) {
        m_renameServer->discoverDetails();
    }

    //防劫持秘钥
    m_antiHijackServer=m_control->createServiceObject(QBluetoothUuid((quint32)BLEServerUUID::BLEAntihijackSer), this);
    if(m_antiHijackServer){
        connect(m_antiHijackServer, &QLowEnergyService::characteristicChanged, this, &DeviceHandler::receiveHijackReturn);
        connect(m_antiHijackServer, &QLowEnergyService::stateChanged, this, &DeviceHandler::serviceStateChanged);
        m_antiHijackServer->discoverDetails();
    }
}

void DeviceHandler::serviceStateChanged(QLowEnergyService::ServiceState s)
{
    switch (s) {
    case QLowEnergyService::DiscoveringServices:
        setInfo(tr("Discovering services..."));
        break;
    case QLowEnergyService::ServiceDiscovered:
    {
        setInfo(tr("Service discovered."));

        const QLowEnergyCharacteristic hrChar = m_service->characteristic(QBluetoothUuid((quint32)BLECharacteristicUUID::BLEReceiveDataCha));
        if (!hrChar.isValid()) {
            setError(languageContent.sCO2CharaNotFoundErr());
            break;
        }
        m_notificationDesc = hrChar.descriptor(QBluetoothUuid::ClientCharacteristicConfiguration);
        if (m_notificationDesc.isValid())
            m_service->writeDescriptor(m_notificationDesc, QByteArray::fromHex("0100"));

        const QLowEnergyCharacteristic chajack = m_antiHijackServer->characteristic(QBluetoothUuid((quint32)BLECharacteristicUUID::BLEAntihijackChaNofi));
        QLowEnergyDescriptor  m_Desc = chajack.descriptor(QBluetoothUuid::ClientCharacteristicConfiguration);
        if (m_Desc.isValid())
            m_antiHijackServer->writeDescriptor(m_Desc, QByteArray::fromHex("0100"));
        writeAntihijackValue("301001301001");

        break;
    }
    default:
        break;
    }

    emit aliveChanged();
}

void DeviceHandler::receiveHijackReturn(const QLowEnergyCharacteristic &c, const QByteArray &value)
{
    if (c.uuid() != QBluetoothUuid((quint32)BLECharacteristicUUID::BLEAntihijackChaNofi))
        return;
    const quint8 *data = reinterpret_cast<const quint8 *>(value.constData());
    switch (data[0]) {
    case 0:
        setInfo("提交密码正确");
        break;
    case 1:
        setError(languageContent.sAntihijackKeyErr());
        writeAntihijackValue("000000301001");
        break;
    default:
        break;
    }
}

void DeviceHandler::confirmedDescriptorWrite(const QLowEnergyDescriptor &d, const QByteArray &value)
{
    if (d.isValid() && d == m_notificationDesc && value == QByteArray::fromHex("0000")) {
        //disabled notifications -> assume disconnect intent
        m_control->disconnectFromDevice();
        delete m_service;
        m_service = 0;
    }
}

void DeviceHandler::disconnectService()
{
    m_foundCO2Service = false;

    //disable notifications
    if (m_notificationDesc.isValid() && m_service
            && m_notificationDesc.value() == QByteArray::fromHex("0100")) {
        m_service->writeDescriptor(m_notificationDesc, QByteArray::fromHex("0000"));
    }

    if (m_control)
        m_control->disconnectFromDevice();

    delete m_service;
    m_service = 0;

}

//-----------属性--------------------
bool DeviceHandler::measuring() const
{
    return m_measuring;
}

bool DeviceHandler::alive() const
{
    if (m_service)
        return m_service->state() == QLowEnergyService::ServiceDiscovered;

    return false;
}

bool DeviceHandler::serverFound() const
{
    return m_foundCO2Service;
}

bool DeviceHandler::breathe() const
{
    return m_Breathe;
}

qreal DeviceHandler::currentCO2() const
{
    return m_currentCO2;
}

qreal DeviceHandler::ETCO2() const
{
    return m_ETCO2;
}

int DeviceHandler::FiCO2() const
{
    return m_FiCO2;
}

int DeviceHandler::RespiratoryRate() const
{
    return m_RespiratoryRate;
}

float DeviceHandler::errorRate() const
{
    return m_errorRate;
}

bool DeviceHandler::ETCO2Alarm()
{
    return m_ETCO2Alarm;
}

bool DeviceHandler::RRAlarm()
{
    return m_RRAlarm;
}

bool DeviceHandler::updateParams()
{
    return m_updateParams;
}

QString DeviceHandler::time()
{
    return m_time;
}

int DeviceHandler::decimal()
{
    return m_decimal;
}

bool DeviceHandler::canZero()
{
    return m_canZero;
}

//QVector<QString> DeviceHandler::co2UnitMode()
//{
//    return m_co2UnitMode;
//}

//QVector<QVector<qreal> > DeviceHandler::co2ScaleMode()
//{
//    return m_co2ScaleMode;
//}

//QVector<QString> DeviceHandler::co2wfSpeedMode()
//{
//    return m_co2wfSpeedMode;
//}

QString DeviceHandler::sCO2Unit()
{
    return m_sCO2Unit;
}

qreal DeviceHandler::CO2Scale()
{
    return m_CO2Scale;
}

int DeviceHandler::wfSpeed()
{
    return m_wfSpeed;
}

int DeviceHandler::CO2UnitIndex()
{
    return m_CO2UnitIndex;
}

int DeviceHandler::CO2ScaleIndex()
{
    return m_CO2ScaleIndex;
}

int DeviceHandler::wfSpeedIndex()
{
    return m_wfSpeedIndex;
}

int DeviceHandler::barometricPressure()
{
    return m_barometricPressure;
}

int DeviceHandler::NoBreaths()
{
    return m_NoBreaths;
}

int DeviceHandler::O2Compensation()
{
    return m_O2Compensation;
}

qreal DeviceHandler::ETCO2Upper()
{
    return m_ETCO2Upper*10;
}

qreal DeviceHandler::ETCO2Lower()
{
    return m_ETCO2Lower*10;
}

int DeviceHandler::RRUpper()
{
    return m_RRUpper*10;
}

int DeviceHandler::RRLower()
{
    return m_RRLower*10;
}

qreal DeviceHandler::etco2UpperFrom()
{
    return m_etco2UpperFrom*10;
}

qreal DeviceHandler::etco2UpperTo()
{
    return m_etco2UpperTo*10;
}

qreal DeviceHandler::etco2UpperStep()
{
    return m_etco2UpperStep*10;
}

qreal DeviceHandler::etco2LowerFrom()
{
    return m_etco2LowerFrom*10;
}

qreal DeviceHandler::etco2LowerTo()
{
    return m_etco2LowerTo*10;
}

qreal DeviceHandler::etco2LowerStep()
{
    return m_etco2LowerStep*10;
}

bool DeviceHandler::screenLock()
{
    return m_screenLock;
}

bool DeviceHandler::asphyxialAlarm()
{
    return m_asphyxialAlarm;
}

bool DeviceHandler::noAdapterAlarm()
{
    return m_noAdapterAlarm;
}

QString DeviceHandler::sFirmwareVersion()
{
    return m_sFirmwareVersion;
}

QString DeviceHandler::sHardwareVersion()
{
    return m_sHardwareVersion;
}

QString DeviceHandler::sProductionDate()
{
    return m_sProductionDate;
}

QString DeviceHandler::sSerialNumber()
{
    return m_sSerialNumber;
}

//--------------------数据--------------------------

void DeviceHandler::analysisCO2WaveDPI1(QByteArray &firstArray)
{
    const quint8 *data = reinterpret_cast<const quint8 *>(firstArray.constData());
    bool alramChange;

    alramChange=(bool)((data[6] & 0x40) == 0x40);
    if(m_asphyxialAlarm!=alramChange){
        m_asphyxialAlarm=alramChange;
        emit alarmChanged();
    }

    alramChange=(bool)((data[6] & 0x02) == 0x02);
    if(m_noAdapterAlarm!=alramChange){
        m_noAdapterAlarm=alramChange;
        emit alarmChanged();
    }
}

void DeviceHandler::updateRangeAlarm()
{
    bool alarmChange;
    if((m_ETCO2>m_ETCO2Upper || m_ETCO2<m_ETCO2Lower) && m_Breathe)
        alarmChange=true;
    else
        alarmChange=false;
    if(m_ETCO2Alarm!=alarmChange){
        m_ETCO2Alarm=alarmChange;
        emit rangeAlarmChanged();
    }

    if((m_RespiratoryRate>m_RRUpper || m_RespiratoryRate<m_RRLower) && m_Breathe)
        alarmChange=true;
    else
        alarmChange=false;
    if(m_RRAlarm!=alarmChange){
        m_RRAlarm=alarmChange;
        emit rangeAlarmChanged();
    }

}



void DeviceHandler::getSpecificValue(QByteArray firstArray)
{
    const quint8 *data = reinterpret_cast<const quint8 *>(firstArray.constData());

    if((int)data[1] != firstArray.count()-2)
        return;

    int cks(0);
    for(int i(0);i<firstArray.size()-1;i++)
        cks+=data[i];
    cks=((~cks+1) & 0x7f);
    if(cks!=data[firstArray.size()-1])
        return;

    switch (data[0]) {
    case SensorCommand::CO2Waveform:{
        m_currentCO2=(qreal)(128*data[3]+data[4]-1000)/100;
        if( m_currentCO2<=m_CO2Scale*0.02)
            m_currentCO2=m_CO2Scale*0.02;

        if(data[1]>4)
        {
            switch (data[5]) {
            case 0x01:
                analysisCO2WaveDPI1(firstArray);
                break;
            case 0x02:
                m_ETCO2=(qreal)(data[6]*128+data[7])/10;
                break;
            case 0x03:
                m_RespiratoryRate=(int)(data[6]*128+data[7]);
                break;
            case 0x04:
                m_FiCO2=(int)(data[6]*128+data[7])/10;
                break;
            case 0x05:
                m_Breathe=true;

            case 0x07:

                break;
            default:
                break;
            }
        }

        updateRangeAlarm();

        emit statsChanged();
    }
        break;
    case SensorCommand::Zero:
        if(data[1]>1)
        {
            switch (data[2]) {
            case 0:
                m_canZero=true;
                break;
            case 1:
                m_canZero=false;
                setError(languageContent.sZeroNoReadyErr());
                break;
            case 2:
                m_canZero=false;
                setError(languageContent.sZeroingErr());
                break;
            case 3:
                m_canZero=false;
                setError(languageContent.sZeroDetectBreathingErr());
                break;
            default:
                break;
            }
            emit zeroStatusChanged();
        }
        break;
    case SensorCommand::Settings:
        switch (data[2]) {
        case 1:
            m_barometricPressure=128*data[3]+data[4];
            //emit sensorParamsChanged();
            break;
        case 6:
            m_NoBreaths=data[3];
            //emit sensorParamsChanged();
            break;
        case 7: //unit
            setCO2Unit(data[3],false);
            updateCO2Scale();
            getAlarmParams();
            changeAlarmRange();
            m_updateParams=true;
            emit displayParamChanged();
            sendContinuous();
            break;
        case 11:
            m_O2Compensation=data[3];
            emit sensorParamsChanged();
            break;
        default:
            break;
        }
        break;
    case SensorCommand::Expand:
        switch (data[2]) {
        case 2:
             m_sFirmwareVersion=QString::number((int)data[3],10) +"."+
                     QString::number((int)data[4],10)+"." + QString::number((int)data[5],10);
             m_sHardwareVersion=QString::number((int)data[6],10)+"." +QString::number((int)data[7],10);
             emit systemParamsChanged();
            break;
        case 4:
            m_sProductionDate=QString::number((int)data[3]+2000,10) +"/"+
                    QString::number((int)data[4],10)+"/"+QString::number((int)data[5],10);
            m_sSerialNumber=QString::number((int)(data[6]*qPow(2,14)+data[7]*qPow(2,7)+data[8]));

            emit systemParamsChanged();
            break;
        case 42://alarm
            setAlarmParams((qreal)(data[3]*128+data[4])/10,(qreal)(data[5]*128+data[6])/10,
                    data[7]*128+data[8],data[9]*128+data[10],false);
            emit alarmParamsChanged();
            sendContinuous();
            break;
        case 44://scale
            //setCO2ScaleByValue((data[3]*128+data[4])/10);
            setCO2ScaleByIndex(data[3],false);
            emit displayParamChanged();
            sendContinuous();
            break;
        default:
            break;
        }
        break;
    default:
        break;
    }

}

void DeviceHandler::CalculateErrorRate(QByteArray getArray)
{
    if(getArray.size()==0)
        return;

    const quint8 *data = reinterpret_cast<const quint8 *>(getArray.constData());
    if((int)data[2]-m_lastNum!=1)
        m_misCount++;
    if((int)data[2]-m_lastNum<0){
        m_errorRate=m_misCount/127*100;
        m_misCount=0;
    }

    m_lastNum=(int)data[2];

    emit statsChanged();
}

void DeviceHandler::updateCO2Value(const QLowEnergyCharacteristic &c, const QByteArray &value)
{
    if (c.uuid() != QBluetoothUuid((quint32)BLECharacteristicUUID::BLEReceiveDataCha))
        return;

    m_receiveBuff.append(QByteArray(value));

    if(m_receiveBuff.size()<20)
        return ;
    else{
        QByteArray firstArray=getFirstArray();
        //CalculateErrorRate(firstArray);
        getSpecificValue(firstArray);
    }

    //qDebug()<<"R:"<<value.toHex(':');

}

QByteArray DeviceHandler::getFirstArray()
{
    QByteArray getArray;

    while(m_receiveBuff.size()>0 && m_receiveBuff.at(0)!=SensorCommand::CO2Waveform
          && m_receiveBuff.at(0)!=SensorCommand::Zero && m_receiveBuff.at(0)!=SensorCommand::Settings
          && m_receiveBuff.at(0)!=SensorCommand::Expand && m_receiveBuff.at(0)!=SensorCommand::StopContinuous
          && m_receiveBuff.at(0)!=SensorCommand::NACKError && m_receiveBuff.at(0)!=SensorCommand::ResetNoBreaths)
        m_receiveBuff.remove(0,1);

    if(m_receiveBuff.size()==0)
        return getArray;

    getArray=m_receiveBuff.left(m_receiveBuff.at(1)+2);
    m_receiveBuff.remove(0,m_receiveBuff.at(1)+2);

    //qDebug()<<"R:"<<getArray.toHex('-');

    return getArray;
}

void DeviceHandler::update(QAbstractSeries *series)
{
    if (series) {
        QXYSeries *xySeries = static_cast<QXYSeries *>(series);

        if(m_index>m_wfSpeed-1){
            m_index=0;
            m_pointsBuffFull=true;
        }
        if(!m_pointsBuffFull){
            points.insert(m_index,QPointF(m_index,m_currentCO2));
        }else{
            points.replace(m_index,QPointF(m_index,m_currentCO2));
        }
        m_index++;

        xySeries->replace(points);
    }
}

void DeviceHandler::moveCursor(QAbstractSeries *series)
{
    if(series){
        QXYSeries *xySeries = static_cast<QXYSeries *>(series);

        if(points.count()==0)
            return;

        if(m_pointsBuffFull){
            cursorPoints.clear();

            if(m_index>0 && m_index<cursorWidth/2)
                cursorPoints=points.mid(0,m_index+cursorWidth/2);
            if(m_index>=cursorWidth/2 && m_index<=m_wfSpeed-cursorWidth/2)
                cursorPoints=points.mid(m_index-cursorWidth/2,cursorWidth);
            if(m_index>m_wfSpeed-cursorWidth/2 && m_index<=m_wfSpeed)
                cursorPoints=points.mid(m_index-cursorWidth/2,m_wfSpeed-m_index+cursorWidth/2);
        }

        xySeries->replace(cursorPoints);
    }
}

void DeviceHandler::appendCKS(QByteArray &arrayData)
{
    const quint8 *data = reinterpret_cast<const quint8 *>(arrayData.constData());

    int cks(0);
    for(int i(0);i<arrayData.size();i++)
        cks+=data[i];
    cks=((~cks+1) & 0x7f);

    arrayData.append(cks);
}

void DeviceHandler::sendBLEData(QByteArray &arrayData)
{
    if(m_sendDataServer){
        const QLowEnergyCharacteristic sendData = m_sendDataServer->characteristic
                (QBluetoothUuid((quint32)BLECharacteristicUUID::BLESendDataCha));
        if (sendData.isValid()) {
            m_sendDataServer->writeCharacteristic(sendData,arrayData);
        }
        //        qDebug()<<"S:"<<arrayData.toHex('-');
    }
}

//-----------设置------------------
void DeviceHandler::shutDown()
{
    QByteArray sendArray;
    sendArray.append(SensorCommand::Reset);
    sendArray.append(0x01);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}

void DeviceHandler::sendStopContinuous()
{
    QByteArray sendArray;
    sendArray.append(SensorCommand::StopContinuous);
    sendArray.append(0x01);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}

void DeviceHandler::sendContinuous()
{
    QByteArray sendArray;
    int zero(0);
    sendArray.append(SensorCommand::CO2Waveform);
    sendArray.append(0x02);
    sendArray.append(zero);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}

void DeviceHandler::getScreenWakeParam()
{
    QSettings settings("config.ini", QSettings::IniFormat);
    if(settings.contains("config/screenWake"))
    {
        QString wake = settings.value("config/screenWake").toString();
        if(wake=="1")
            m_screenLock=true;
        else
            m_screenLock=false;
    }else
        m_screenLock=true;
    setScreenWakeLock();
}

void DeviceHandler::setScreenWakeParam(bool lock)
{
    QSettings settings("config.ini", QSettings::IniFormat);
    settings.setValue("config/screenWake",QString::number(lock));
    m_screenLock=lock;
    setScreenWakeLock();
}

void DeviceHandler::writeZeroValue()
{
    QByteArray sendArray;
    sendArray.append(SensorCommand::Zero);
    sendArray.append(0x01);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}

void DeviceHandler::renameServer(QString newServerName)
{  
    if (m_renameServer){
        const QLowEnergyCharacteristic rename = m_renameServer->characteristic
                (QBluetoothUuid((quint32)BLECharacteristicUUID::BLERenameCha));
        if (rename.isValid()) {
            m_renameServer->writeCharacteristic(rename,QByteArray::fromStdString(newServerName.toStdString()));
        }
    }
}

void DeviceHandler::writeCorrectValue()
{
    QByteArray sendArray;
    sendArray.append(SensorCommand::Expand);
    sendArray.append(0x02);
    sendArray.append(0x08);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}

void DeviceHandler::setAlarmParams(qreal etco2upper, qreal etco2lower, int rrupper, int rrlower,bool send)
{
    if((etco2lower>etco2upper) || (rrlower>rrupper)){
        setError(languageContent.sSetAlarmParamsErr());
        return;
    }

    m_ETCO2Upper=etco2upper;
    m_ETCO2Lower=etco2lower;
    m_RRUpper=rrupper;
    m_RRLower=rrlower;

    if(send){
        QByteArray sendArray;
        sendArray.append(SensorCommand::Expand);
        sendArray.append(10);
        sendArray.append(42);
        sendArray.append((int)etco2upper*10>>7);
        sendArray.append((int)(etco2upper*10)&0x7f);
        sendArray.append((int)etco2lower*10>>7);
        sendArray.append((int)(etco2lower*10)&0x7f);
        sendArray.append((int)rrupper>>7);
        sendArray.append((int)rrupper&0x7f);
        sendArray.append((int)rrlower>>7);
        sendArray.append(rrlower&0x7f);
        appendCKS(sendArray);
        sendBLEData(sendArray);
    }

}

void DeviceHandler::getAlarmParams()
{
    QByteArray sendArray;
    sendArray.append(SensorCommand::Expand);
    sendArray.append(0x02);
    sendArray.append(42);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}

void DeviceHandler::convertAlarmValue(int changedUnitIndex)
{
    switch (changedUnitIndex) {
    case 0:
        switch (m_CO2UnitIndex) {
        case 1:
            m_ETCO2Upper=m_ETCO2Upper*760/101.3;
            break;
        case 2:
            m_ETCO2Upper=m_ETCO2Upper*760/100;
            break;
        default:
            break;
        }
        break;
    case 1:
        switch (m_CO2UnitIndex) {
        case 0:
            m_ETCO2Upper=m_ETCO2Upper*101.3/760;
            break;
        case 2:
            m_ETCO2Upper=m_ETCO2Upper*101.3/100;
            break;
        default:
            break;
        }
        break;
        break;
    case 2:
        switch (m_CO2UnitIndex) {
        case 0:
            m_ETCO2Upper=m_ETCO2Upper*100/760;
            break;
        case 1:
            m_ETCO2Upper=m_ETCO2Upper*100/101.3;
            break;
        default:
            break;
        }
        break;
        break;
    default:
        break;
    }
}

void DeviceHandler::changeAlarmRange()
{
    switch (m_CO2UnitIndex) {
    case 0: //mmHg
        m_etco2UpperFrom=1;
        m_etco2UpperStep=1;
        m_etco2UpperTo=99;
        m_etco2LowerFrom=1;
        m_etco2LowerStep=1;
        m_etco2LowerTo=99;
        m_decimal=0;
        break;
    case 1: //Kpa
        m_etco2UpperFrom=0.1;
        m_etco2UpperStep=0.1;
        m_etco2UpperTo=13.2;
        m_etco2LowerFrom=0.1;
        m_etco2LowerStep=0.1;
        m_etco2LowerTo=13.2;
        m_decimal=1;
        break;
    case 2: //%
        m_etco2UpperFrom=0.1;
        m_etco2UpperStep=0.1;
        m_etco2UpperTo=13.0;
        m_etco2LowerFrom=0.1;
        m_etco2LowerStep=0.1;
        m_etco2LowerTo=13.0;
        m_decimal=1;
        break;
    default:
        break;
    }
}

//---------显示参数----------------
void DeviceHandler::updateCO2Unit()
{
    QByteArray sendArray;
    sendArray.append(SensorCommand::Settings);
    sendArray.append(0x02);
    sendArray.append(0x07);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}

void DeviceHandler::updateCO2Scale()
{
    QByteArray sendArray;
    sendArray.append(SensorCommand::Expand);
    sendArray.append(0x02);
    sendArray.append(44);
    //appendCKS(sendArray);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}

void DeviceHandler::updatewfSpeed()
{

}

void DeviceHandler::setCO2Unit(int index,bool send)
{
    m_sCO2Unit=m_co2UnitMode[index];
    m_CO2UnitIndex=index;

    if(send){
        QByteArray sendArray;
        sendArray.append(SensorCommand::Settings);
        sendArray.append(0x03);
        sendArray.append(0x07);
        sendArray.append(index);
        appendCKS(sendArray);
        sendBLEData(sendArray);
    }

}

void DeviceHandler::setCO2ScaleByIndex(int scale,bool send)
{
    m_CO2Scale=m_co2ScaleMode[m_CO2UnitIndex][scale];
    m_CO2ScaleIndex=scale;
  //  changeAlarmRange();
    if(send)
    {
        QByteArray sendArray;
        sendArray.append(SensorCommand::Expand);
        sendArray.append(0x03);
        sendArray.append(44);
        sendArray.append(scale);
        //        sendArray.append((int)m_CO2Scale*10>>7);
        //        sendArray.append((int)(m_CO2Scale*10)&0x7f);
        appendCKS(sendArray);
        sendBLEData(sendArray);
    }
}

void DeviceHandler::setCO2ScaleByValue(int value)
{
    QVector<qreal> scaleMode=m_co2ScaleMode[m_CO2UnitIndex];
    m_CO2ScaleIndex=scaleMode.indexOf(value);
}

void DeviceHandler::setSpeed(int speed)
{
    cursorPoints.clear();
    cursorPoints.reserve(cursorWidth);
    m_wfSpeed=speed;
    points.clear();
    points.reserve(m_wfSpeed);
    m_index=0;
    m_pointsBuffFull=false;
}

void DeviceHandler::setWFSpeed(int speed,bool send)
{
    switch (speed) {
    case 0:
        setSpeed(500);
        m_time="10S";
        break;
    case 1:
        setSpeed(1000);
        m_time="20S";
        break;
    case 2:
        setSpeed(1500);
        m_time="30S";
        break;
    default:
        break;
    }
    m_wfSpeedIndex=speed;
}

void DeviceHandler::writeAntihijackValue(QString jackValue)
{
    if (m_antiHijackServer){
        const QLowEnergyCharacteristic hijack = m_antiHijackServer->characteristic
                (QBluetoothUuid((quint32)BLECharacteristicUUID::BLEAntihijackCha));
        if (hijack.isValid()) {
            m_antiHijackServer->writeCharacteristic(hijack,QByteArray::fromStdString(jackValue.toStdString()));
        }
    }
}

void DeviceHandler::setDisplayParams(int unit, int scale, int speed)
{
    sendStopContinuous();
    setCO2Unit(unit,true);
    setCO2ScaleByIndex(scale,true);
    setWFSpeed(speed,true);
    sendContinuous();
    // setCO2ScaleByValue(60);
}

//------------模块参数------------------
void DeviceHandler::getSensorParams()
{
    //sendStopContinuous();
    updateCO2Unit();
    //updateCO2Scale();
    //getAlarmParams();
}

void DeviceHandler::getSystemParams()
{
    QByteArray sendArray;
    sendArray.append(SensorCommand::Expand);
    sendArray.append(0x02);
    sendArray.append(0x02);
    appendCKS(sendArray);
    sendBLEData(sendArray);

    sendArray.clear();
    sendArray.append(SensorCommand::Expand);
    sendArray.append(0x02);
    sendArray.append(0x04);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}

void DeviceHandler::setScreenWakeLock()
{
    QAndroidJniEnvironment env;
    QAndroidJniObject activity = QtAndroid::androidActivity();

    if(!m_screenLock){
        if(m_wakeLock.isValid())
        {
            m_wakeLock.callMethod<void>("release");
            return;
        }
    }

    QAndroidJniObject name = QAndroidJniObject::getStaticObjectField(
                "android/content/Context",
                "POWER_SERVICE",
                "Ljava/lang/String;"
                );
    QAndroidJniObject powerService = activity.callObjectMethod(
                "getSystemService",
                "(Ljava/lang/String;)Ljava/lang/Object;",
                name.object<jstring>());
    QAndroidJniObject tag = QAndroidJniObject::fromString("QtJniWakeLock");
    m_wakeLock = powerService.callObjectMethod(
                "newWakeLock",
                "(ILjava/lang/String;)Landroid/os/PowerManager$WakeLock;",
                10, //SCREEN_BRIGHT_WAKE_LOCK
                tag.object<jstring>()
                );
    if(m_screenLock){
        if(m_wakeLock.isValid())
        {
            m_wakeLock.callMethod<void>("acquire");
        }
    }
}

void DeviceHandler::setSensorParams(int BP, int noBreaths, int o2Compensation)
{
    setBarometricPressure(BP);
    setNoBreaths(noBreaths);
    setO2Compensation(o2Compensation);
}

void DeviceHandler::setBarometricPressure(int param)
{

}

void DeviceHandler::setNoBreaths(int param)
{
    QByteArray sendArray;
    sendArray.append(SensorCommand::Settings);
    sendArray.append(0x03);
    sendArray.append(0x06);
    sendArray.append(param);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}

void DeviceHandler::setO2Compensation(int param)
{
    QByteArray sendArray;
    int zero(0);
    sendArray.append(SensorCommand::Settings);
    sendArray.append(0x06);
    sendArray.append(0x0b);
    sendArray.append(param);
    sendArray.append(zero);
    sendArray.append(zero);
    sendArray.append(zero);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}

void DeviceHandler::updateBarometricPressure()
{
    QByteArray sendArray;
    sendArray.append(SensorCommand::Settings);
    sendArray.append(0x02);
    sendArray.append(0x01);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}

void DeviceHandler::updateNoBreaths()
{
    QByteArray sendArray;
    sendArray.append(SensorCommand::Settings);
    sendArray.append(0x02);
    sendArray.append(0x06);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}

void DeviceHandler::updateO2Compensation()
{
    QByteArray sendArray;
    sendArray.append(SensorCommand::Settings);
    sendArray.append(0x02);
    sendArray.append(0x0B);
    appendCKS(sendArray);
    sendBLEData(sendArray);
}
