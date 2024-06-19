#ifndef DEVICEHANDLER_H
#define DEVICEHANDLER_H

#include "bluetoothbaseclass.h"

#include <qmediaplayer.h>
#include <qmath.h>

#include <QtAndroid>
#include <QAndroidJniEnvironment>
#include <QAndroidJniObject>
#include <QAndroidActivityResultReceiver>
#include <QtAndroidExtras>

#include <QDateTime>
#include <QVector>
#include <QTimer>
#include <QLowEnergyController>
#include <QLowEnergyService>
#include <QtCore/QObject>
#include <QtCharts/QAbstractSeries>

QT_BEGIN_NAMESPACE
class QQuickView;
QT_END_NAMESPACE
QT_CHARTS_USE_NAMESPACE

class DeviceInfo;

class DeviceHandler : public BluetoothBaseClass
{
    Q_OBJECT

    Q_PROPERTY(bool connectSuccess READ connectSuccess NOTIFY connectChanged)
    Q_PROPERTY(QString currentDeviceName READ currentDeviceName NOTIFY serverFoundChanged)
    Q_PROPERTY(bool serverFound READ serverFound NOTIFY serverFoundChanged)
    Q_PROPERTY(bool measuring READ measuring NOTIFY measuringChanged)
    Q_PROPERTY(bool alive READ alive NOTIFY aliveChanged)
    Q_PROPERTY(bool breathe READ breathe NOTIFY breatheChanged)
    Q_PROPERTY(qreal currentCO2 READ currentCO2 NOTIFY statsChanged)
    Q_PROPERTY(qreal ETCO2 READ ETCO2 NOTIFY statsChanged)
    Q_PROPERTY(int FiCO2 READ FiCO2 NOTIFY statsChanged)
    Q_PROPERTY(int RespiratoryRate READ RespiratoryRate NOTIFY statsChanged)
    Q_PROPERTY(int errorRate READ errorRate NOTIFY statsChanged)
    Q_PROPERTY(AddressType addressType READ addressType WRITE setAddressType)

    Q_PROPERTY(bool screenLock READ screenLock WRITE setScreenWakeParam)

    Q_PROPERTY(int decimal READ decimal NOTIFY displayParamChanged)
    Q_PROPERTY(QString time READ time NOTIFY displayParamChanged)
    Q_PROPERTY(bool updateParams READ updateParams NOTIFY displayParamChanged)
    Q_PROPERTY(QString sCO2Unit READ sCO2Unit NOTIFY displayParamChanged)
    Q_PROPERTY(qreal CO2Scale READ CO2Scale NOTIFY displayParamChanged)
    Q_PROPERTY(int wfSpeed READ wfSpeed NOTIFY displayParamChanged)
    Q_PROPERTY(int CO2UnitIndex READ CO2UnitIndex NOTIFY displayParamChanged)
    Q_PROPERTY(int CO2ScaleIndex READ CO2ScaleIndex NOTIFY displayParamChanged)
    Q_PROPERTY(int wfSpeedIndex READ wfSpeedIndex NOTIFY displayParamChanged)

    Q_PROPERTY(int barometricPressure READ barometricPressure NOTIFY sensorParamsChanged)
    Q_PROPERTY(int NoBreaths READ NoBreaths NOTIFY sensorParamsChanged)
    Q_PROPERTY(int O2Compensation READ O2Compensation NOTIFY sensorParamsChanged)

    Q_PROPERTY(qreal ETCO2Upper READ ETCO2Upper NOTIFY alarmParamsChanged)
    Q_PROPERTY(qreal ETCO2Lower READ ETCO2Lower NOTIFY alarmParamsChanged)
    Q_PROPERTY(int RRUpper READ RRUpper NOTIFY alarmParamsChanged)
    Q_PROPERTY(int RRLower READ RRLower NOTIFY alarmParamsChanged)
    Q_PROPERTY(qreal etco2UpperFrom READ etco2UpperFrom NOTIFY alarmParamsChanged)
    Q_PROPERTY(qreal etco2UpperTo READ etco2UpperTo NOTIFY alarmParamsChanged)
    Q_PROPERTY(qreal etco2UpperStep READ etco2UpperStep NOTIFY alarmParamsChanged)
    Q_PROPERTY(qreal etco2LowerFrom READ etco2LowerFrom NOTIFY alarmParamsChanged)
    Q_PROPERTY(qreal etco2LowerTo READ etco2LowerTo NOTIFY alarmParamsChanged)
    Q_PROPERTY(qreal etco2LowerStep READ etco2LowerStep NOTIFY alarmParamsChanged)

    Q_PROPERTY(bool ETCO2Alarm READ ETCO2Alarm NOTIFY rangeAlarmChanged)
    Q_PROPERTY(bool RRAlarm READ RRAlarm NOTIFY rangeAlarmChanged)
    Q_PROPERTY(bool asphyxialAlarm READ asphyxialAlarm NOTIFY alarmChanged)
    Q_PROPERTY(bool noAdapterAlarm READ noAdapterAlarm NOTIFY alarmChanged)

    Q_PROPERTY(QString sFirmwareVersion READ sFirmwareVersion NOTIFY systemParamsChanged)
    Q_PROPERTY(QString sHardwareVersion READ sHardwareVersion NOTIFY systemParamsChanged)
    Q_PROPERTY(QString sProductionDate READ sProductionDate NOTIFY systemParamsChanged)
    Q_PROPERTY(QString sSerialNumber READ sSerialNumber NOTIFY systemParamsChanged)

    Q_PROPERTY(bool canZero READ canZero NOTIFY zeroStatusChanged)

public:
    enum class AddressType {
        PublicAddress,
        RandomAddress
    };
    Q_ENUM(AddressType)
    enum BLEServerUUID{
        BLESendDataSer =0xFFE5,
        BLEReceiveDataSer=0xFFE0,
        BLEModuleParamsSer=0xFF90,
        BLEAntihijackSer=0xFFC0
    };
    enum BLECharacteristicUUID{
        BLESendDataCha=0xFFE9,
        BLEReceiveDataCha=0xFFE4,
        BLERenameCha=0xFF91,
        BLEBaudCha =0xFF93,
        BLEAntihijackChaNofi=0xFFC2,
        BLEAntihijackCha=0xFFC1
    };
    enum SensorCommand{
        CO2Waveform=0x80,
        Zero=0x82,
        Expand=0xF2,
        Settings=0x84,
        StopContinuous=0xC9,
        NACKError=0xC8,
        ResetNoBreaths=0xCC,
        Reset=0xF8
    };


    DeviceHandler(QObject *parent = 0);

    void setDevice(DeviceInfo *device);
    void setAddressType(AddressType type);
    AddressType addressType() const;

    QString currentDeviceName();
    bool connectSuccess();
    bool measuring() const;
    bool alive() const;
    bool serverFound() const;
    bool breathe() const;
    qreal currentCO2() const;
    qreal ETCO2() const;
    int FiCO2()const;
    int RespiratoryRate() const;
    float errorRate() const;
    bool ETCO2Alarm();
    bool RRAlarm();
    bool updateParams();
    QString time();
    int decimal();
    bool canZero();

    QString  sCO2Unit();
    qreal CO2Scale();
    int wfSpeed();
    int CO2UnitIndex();
    int CO2ScaleIndex();
    int wfSpeedIndex();
    int barometricPressure();
    int NoBreaths();
    int O2Compensation();

    qreal ETCO2Upper();
    qreal ETCO2Lower();
    int RRUpper();
    int RRLower();
    qreal etco2UpperFrom();
    qreal etco2UpperTo();
    qreal etco2UpperStep();
    qreal etco2LowerFrom();
    qreal etco2LowerTo();
    qreal etco2LowerStep();

    bool screenLock();
    //alarm
    bool asphyxialAlarm();
    bool noAdapterAlarm();

    QString sFirmwareVersion();
    QString sHardwareVersion();
    QString sProductionDate();
    QString sSerialNumber();

signals:
    void hideMessage();
    void measuringChanged();
    void aliveChanged();
    void statsChanged();
    void serverFoundChanged();
    void connectChanged();
    void breatheChanged();
    void displayParamChanged();
    void sensorParamsChanged();
    void alarmParamsChanged();
    void alarmChanged();
    void rangeAlarmChanged();
    void systemParamsChanged();
    void zeroStatusChanged();

public slots:
    void startMeasurement();
    void stopMeasurement();
    void disconnectService();
    void update(QAbstractSeries *series);
    void moveCursor(QAbstractSeries *series);
    void shutDown();
    void writeZeroValue();
    void writeCorrectValue();
    void updateCO2Unit();
    void updateCO2Scale();
    void updatewfSpeed();
    void setDisplayParams(int unit,int scale,int speed);
    void setSensorParams(int BP,int noBreaths,int o2Compensation);
    void setBarometricPressure(int param);
    void setNoBreaths(int param);
    void setO2Compensation(int param);
    void updateBarometricPressure();
    void updateNoBreaths();
    void updateO2Compensation();
    void setAlarmParams(qreal etco2upper,qreal etco2lower,int rrupper,int rrlower,bool send);
    void getAlarmParams();
    void convertAlarmValue(int changedUnit);
    void changeAlarmRange();
    void getSensorParams();
    void getSystemParams();
    void renameServer(QString newServerName);

private:
    //QLowEnergyController
    void serviceDiscovered(const QBluetoothUuid &);
    void serviceScanDone();

    //QLowEnergyService
    void serviceStateChanged(QLowEnergyService::ServiceState s);
    void receiveHijackReturn(const QLowEnergyCharacteristic &c,
                              const QByteArray &value);
    void updateCO2Value(const QLowEnergyCharacteristic &c,
                              const QByteArray &value);
    void confirmedDescriptorWrite(const QLowEnergyDescriptor &d,
                              const QByteArray &value);

private:
    void getSpecificValue(QByteArray firstArray);
    QByteArray getFirstArray();
    void CalculateErrorRate(QByteArray getArray);
    void sendBLEData(QByteArray &arrayData);
    void appendCKS(QByteArray &arrayData);
    void setSpeed(int speed);
    void setCO2Unit(int uint,bool send);
    void setCO2ScaleByIndex(int scale,bool send);
    void setCO2ScaleByValue(int value);
    void setWFSpeed(int speed,bool send);
    void writeAntihijackValue(QString jackValue);
    void sendStopContinuous();
    void sendContinuous();
    void getScreenWakeParam();
    void setScreenWakeParam(bool lock);
    void setScreenWakeLock();
    void analysisCO2WaveDPI1(QByteArray &firstArray);
    void updateRangeAlarm();

    QLowEnergyController *m_control;
    QLowEnergyService *m_service;
    QLowEnergyDescriptor m_notificationDesc;
    DeviceInfo *m_currentDevice;
    QLowEnergyService *m_renameServer;
    QLowEnergyService *m_sendDataServer;
    QLowEnergyService *m_antiHijackServer;

    QString m_currentDeviceName;
    bool m_updateParams;
    bool m_connectSuccess;
    bool m_foundCO2Service;
    bool m_measuring;
    qreal m_currentCO2;
    int m_RespiratoryRate;
    int m_FiCO2;
    qreal m_ETCO2;
    bool m_Breathe;
    bool m_pointsBuffFull;
    bool m_ETCO2Alarm;
    bool m_RRAlarm;
    QString m_time;
    int m_decimal;
    bool m_canZero;

    QString m_sCO2Unit;
    qreal m_CO2Scale;
    int m_wfSpeed;
    int m_CO2UnitIndex;
    int m_CO2ScaleIndex;
    int m_wfSpeedIndex;

    int m_barometricPressure;
    int m_NoBreaths;
    int m_O2Compensation;

    qreal m_ETCO2Upper;
    qreal m_ETCO2Lower;
    int m_RRUpper;
    int m_RRLower;
    qreal m_etco2UpperFrom;
    qreal m_etco2UpperTo;
    qreal m_etco2UpperStep;
    qreal m_etco2LowerFrom;
    qreal m_etco2LowerTo;
    qreal m_etco2LowerStep;

    //error rate
    float m_misCount;
    int m_lastNum;
    float m_errorRate;

    //points
    int m_index;
    QVector<QPointF> points;
    // cursor
    int cursorWidth;
    QVector<QPointF> cursorPoints;
    QByteArray m_receiveBuff;
    QLowEnergyController::RemoteAddressType m_addressType = QLowEnergyController::PublicAddress;

    QVector<qreal> m_co2UnitmmHgscaleMode;
    QVector<qreal> m_co2UnitkpascaleMode;
    QVector<qreal> m_co2UnitpercentscaleMode;

    QVector<QString> m_co2UnitMode;
    QVector<QVector<qreal>> m_co2ScaleMode;
    QVector<QString> m_co2wfSpeedMode;

    QAndroidJniObject m_wakeLock;
    bool m_screenLock;

    //alarm
    bool m_asphyxialAlarm;
    bool m_noAdapterAlarm;

    //system
    QString m_sFirmwareVersion;
    QString m_sHardwareVersion;
    QString m_sProductionDate;
    QString m_sSerialNumber;
};

#endif // DEVICEHANDLER_H
