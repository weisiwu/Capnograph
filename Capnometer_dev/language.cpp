#include "language.h"
#include <QLocale>

Language::Language(QObject *parent) : QObject(parent)
{
    QLocale locale;
//    if( locale.language() == QLocale::Chinese || locale.language() == QLocale::AnyLanguage)
//        m_isChinese=true;
//    else
//        m_isChinese=false;

    GetLanguageParam();
    ChangeLanguage();
}

void Language::ChangeLanguage()
{
    if(!m_isChinese)
    {
        m_sSearch="Search";
        m_sSettings="Settings";
        m_sQuit="Press the exit again.";
        m_sCurrentDevice="Device:";

        m_sFoundDevices="Found Devices";
        m_sStartSearch="Start Search";
        m_sBLEConnect="BLE Connect";
        m_sZero="Zeroing";
        m_sAlarmParams="Alarm Params";
        m_sDisplayParams="Display Params";
        m_sModuleParams="Sensor Params";
        m_sPowerOff="Power Off";
        m_sOn="On";
        m_sOff="Off";
        m_sUpdate="Update";
        m_sSet="Set";
        m_sLanguage="Language";
        m_sCorrect="Correct";
        m_sDebug="Debug";
        m_sPSInput="Please input the password";
        m_sSystemSet="System Settings";
        m_sScreenWakeLock="Screen Bright hold on";
        m_sVersion="Version";
        m_sFirmwareVersion="Firmware Version";
        m_sHardwareVersion="Hardware Version";
        m_sProductionDate="Production Date";
        m_sSerialNumber="Serial Number";

        m_OK="OK";
        m_cancel="Cancel";
        m_sPSerror="Wrong password";

        m_sBarometricPressure="Barometric Pressure";
        m_sAsphyxialTime="Apnea Time";
        m_sOxygenCompensation="O2 Compensation";
        m_sModleName="ModleName";

        m_sBLEEnableMes="Please open the bluetooth of your phone.";
        m_sDialogTitleMes="Warning";
        m_sCloseModuleMes="Shut down?";
        m_sConnectSuccess="Please wait...";
        m_sServerFound = "CO2 service discovered.";

        m_sBLEControllerDisconnectErr="Device disconnected";
        m_sCO2ServerNotFoundErr="CO2 Service is not founded.";
        m_sCO2CharaNotFoundErr="Characteristic is not founded.";
        m_sBLEAdaptorPowerOffErr="The Bluetooth adaptor is powered off.";
        m_sBLEReadWriteErr="Writing or reading from the device resulted in an error.";
        m_sBLEUnknownErr="An unknown error has occurred.";
        m_sBLENoFoundErr="No devices is founded.";
        m_sDisconnectErr="LowEnergy controller disconnected";
        m_sAntihijackKeyErr="Anti-hijacking key error";
        m_sSetAlarmParamsErr="The lower limit is higher than the upper limit";

        m_sZeroNoReadyErr="Sensor not ready for zeroing.";
        m_sZeroingErr="Zero in progress.";
        m_sZeroDetectBreathingErr="Not ready! Try again later!";

        m_sAsphyxialAlarm="Apnea";
        m_sNoAdapterAlarm="NO Adapter";
    }else{
        m_sSearch="搜索";
        m_sSettings="设置";
        m_sQuit="再按一次退出.";
        m_sCurrentDevice="设备:";

        m_sFoundDevices="可用设备";
        m_sStartSearch="搜索";
        m_sBLEConnect="蓝牙连接";
        m_sZero="校零";
        m_sAlarmParams="报警参数";
        m_sDisplayParams="显示参数";
        m_sModuleParams="模块参数";
        m_sPowerOff="关机";
        m_sOn="开";
        m_sOff="关";
        m_sUpdate="更新";
        m_sSet="设置";
        m_sLanguage="语言";
        m_sCorrect="校正";
        m_sDebug="调试";
        m_sPSInput="请输入密码";
        m_sSystemSet="系统设置";
        m_sScreenWakeLock="屏幕常亮";
        m_sVersion="软件版本";
        m_sFirmwareVersion="固件版本";
        m_sHardwareVersion="硬件版本";
        m_sProductionDate="生产日期";
        m_sSerialNumber="序列号";

        m_OK="确定";
        m_cancel="取消";
        m_sPSerror="密码错误";

        m_sBarometricPressure="大气压";
        m_sAsphyxialTime="窒息时间";
        m_sOxygenCompensation="氧气补偿";
        m_sModleName="模块名称";

        m_sBLEEnableMes="请打开手机蓝牙";
        m_sDialogTitleMes="提示";
        m_sCloseModuleMes="您确定要关闭模块吗?";
        m_sConnectSuccess="请稍后...";
        m_sServerFound="发现CO2服务";

        m_sBLEControllerDisconnectErr="设备已断开！";
        m_sCO2ServerNotFoundErr="没有发现CO2服务！";
        m_sCO2CharaNotFoundErr="没有发现CO2特性通道！";
        m_sBLEAdaptorPowerOffErr="蓝牙适配器没有打开！";
        m_sBLEReadWriteErr="从设备中读写结果错误！";
        m_sBLEUnknownErr="未知错误！";
        m_sBLENoFoundErr="没有找到设备！";
        m_sDisconnectErr="蓝牙断开连接!";
        m_sAntihijackKeyErr="防劫持秘钥错误!";
        m_sSetAlarmParamsErr="下限值比上限值高!";

        m_sZeroNoReadyErr="模块还未准备好校零!";
        m_sZeroingErr="模块正在校零!";
        m_sZeroDetectBreathingErr="模块未准备好较零，请20秒后再试!";

        m_sAsphyxialAlarm="窒息";
        m_sNoAdapterAlarm="无适配器";
    }
emit languageChange();
}

void Language::GetLanguageParam()
{
     QSettings settings("config.ini", QSettings::IniFormat);
      if(settings.contains("config/language"))
      {
           QString lan = settings.value("config/language").toString();
           if(lan=="1")
               m_isChinese=true;
           else
               m_isChinese=false;
      }
      else
         m_isChinese=false;
}

void Language::SetLanguageParam(QString param)
{
    QSettings settings("config.ini", QSettings::IniFormat);
    settings.setValue("config/language",param);
}

bool Language::isChinese()
{
    return m_isChinese;
}

void Language::setLanguage(bool language)
{
    m_isChinese=language;
    ChangeLanguage();
    SetLanguageParam(QString::number(language));
}

QString Language::sSearch() const
{
    return m_sSearch;
}

QString Language::sSettings() const
{
    return m_sSettings;
}

QString Language::sQuit() const
{
    return m_sQuit;
}

QString Language::sCurrentDevice() const
{
    return m_sCurrentDevice;
}


QString Language::sFoundDevices() const
{
    return m_sFoundDevices;
}

QString Language::sStartSearch() const
{
    return m_sStartSearch;
}

QString Language::sBLEConnect() const
{
    return m_sBLEConnect;
}

QString Language::sZero() const
{
    return m_sZero;
}

QString Language::sAlarmParams() const
{
    return m_sAlarmParams;
}

QString Language::sDisplayParams() const
{
    return m_sDisplayParams;
}

QString Language::sModuleParams() const
{
    return m_sModuleParams;
}

QString Language::sPowerOff() const
{
    return m_sPowerOff;
}

QString Language::sOn() const
{
    return m_sOn;
}

QString Language::sOff() const
{
    return m_sOff;
}

QString Language::sUpdate() const
{
    return m_sUpdate;
}

QString Language::sSet() const
{
    return m_sSet;
}
QString Language::sLanguage() const
{
    return m_sLanguage;
}

QString Language::sCorrect() const
{
    return m_sCorrect;
}

QString Language::sDebug() const
{
    return m_sDebug;
}

QString Language::sPSInput() const
{
    return m_sPSInput;
}

QString Language::sSystemSet() const
{
    return m_sSystemSet;
}

QString Language::sScreenWakeLock() const
{
    return m_sScreenWakeLock;
}

QString Language::sVersion()
{
    return m_sVersion;
}

QString Language::OK() const
{
    return m_OK;
}

QString Language::cancel() const
{
    return m_cancel;
}
QString Language::sBarometricPressure() const
{
    return m_sBarometricPressure;
}
QString Language::sAsphyxialTime() const
{
    return m_sAsphyxialTime;
}
QString Language::sOxygenCompensation() const
{
    return m_sOxygenCompensation;
}

QString Language::sModleName() const
{
    return m_sModleName;
}
QString Language::sBLEEnableMes() const
{
    return m_sBLEEnableMes;
}
QString Language::sDialogTitleMes() const
{
    return m_sDialogTitleMes;
}
QString Language::sCloseModuleMes() const
{
    return m_sCloseModuleMes;
}

QString Language::sConnectSuccess() const
{
    return m_sConnectSuccess;
}

QString Language::sServerFound() const
{
    return m_sServerFound;
}

QString Language::sPSerror() const
{
    return m_sPSerror;
}

QString Language::sCO2ServerNotFoundErr() const
{
    return m_sCO2ServerNotFoundErr;
}
QString Language::sCO2CharaNotFoundErr() const
{
    return m_sCO2CharaNotFoundErr;
}
QString Language::sBLEControllerDisconnectErr() const
{
    return m_sBLEControllerDisconnectErr;
}
QString Language::sBLEAdaptorPowerOffErr() const
{
    return m_sBLEAdaptorPowerOffErr;
}
QString Language::sBLEReadWriteErr() const
{
    return m_sBLEReadWriteErr;
}
QString Language::sBLEUnknownErr() const
{
    return m_sBLEUnknownErr;
}
QString Language::sBLENoFoundErr() const
{
    return m_sBLENoFoundErr;
}

QString Language::sDisconnectErr() const
{
    return m_sDisconnectErr;
}

QString Language::sAntihijackKeyErr() const
{
    return m_sAntihijackKeyErr;
}

QString Language::sSetAlarmParamsErr() const
{
    return m_sSetAlarmParamsErr;
}

QString Language::sZeroNoReadyErr()
{
    return m_sZeroNoReadyErr;
}

QString Language::sZeroingErr()
{
    return m_sZeroingErr;
}

QString Language::sZeroDetectBreathingErr()
{
    return m_sZeroDetectBreathingErr;
}

QString Language::sNoAdapterAlarm() const
{
    return m_sNoAdapterAlarm;
}

QString Language::sAsphyxialAlarm() const
{
    return m_sAsphyxialAlarm;
}

QString Language::sFirmwareVersion() const
{
    return m_sFirmwareVersion;
}

QString Language::sHardwareVersion() const
{
    return m_sHardwareVersion;
}

QString Language::sProductionDate() const
{
    return m_sProductionDate;
}

QString Language::sSerialNumber() const
{
    return m_sSerialNumber;
}


