#ifndef LANGUAGE_H
#define LANGUAGE_H

#include <QString>
#include <QObject>
#include <QSettings>

//#define _English

class Language:public QObject
{
    Q_OBJECT

    Q_PROPERTY(bool isChinese READ isChinese WRITE setLanguage )

    Q_PROPERTY(QString sSearch READ sSearch NOTIFY languageChange)
    Q_PROPERTY(QString sSettings READ sSettings NOTIFY languageChange)
    Q_PROPERTY(QString sQuit READ sQuit NOTIFY languageChange)
    Q_PROPERTY(QString sCurrentDevice READ sCurrentDevice NOTIFY languageChange)

    Q_PROPERTY(QString sFoundDevices READ sFoundDevices NOTIFY languageChange)
    Q_PROPERTY(QString sStartSearch READ sStartSearch NOTIFY languageChange)

    Q_PROPERTY(QString sBLEConnect READ sBLEConnect NOTIFY languageChange)
    Q_PROPERTY(QString sZero READ sZero NOTIFY languageChange)
    Q_PROPERTY(QString sAlarmParams READ sAlarmParams NOTIFY languageChange)
    Q_PROPERTY(QString sDisplayParams READ sDisplayParams NOTIFY languageChange)
    Q_PROPERTY(QString sModuleParams READ sModuleParams NOTIFY languageChange)
    Q_PROPERTY(QString sPowerOff READ sPowerOff NOTIFY languageChange)
    Q_PROPERTY(QString sOn READ sOn NOTIFY languageChange)
    Q_PROPERTY(QString sOff READ sOff NOTIFY languageChange)
    Q_PROPERTY(QString sUpdate READ sUpdate NOTIFY languageChange)
    Q_PROPERTY(QString sSet READ sSet NOTIFY languageChange)
    Q_PROPERTY(QString sLanguage READ sLanguage NOTIFY languageChange)
    Q_PROPERTY(QString sCorrect READ sCorrect NOTIFY languageChange)
    Q_PROPERTY(QString sDebug READ sDebug NOTIFY languageChange)
    Q_PROPERTY(QString sPSInput READ sPSInput NOTIFY languageChange)
    Q_PROPERTY(QString sSystemSet READ sSystemSet NOTIFY languageChange)
    Q_PROPERTY(QString sScreenWakeLock READ sScreenWakeLock NOTIFY languageChange)
    Q_PROPERTY(QString sVersion READ sVersion NOTIFY languageChange)

    Q_PROPERTY(QString OK READ OK NOTIFY languageChange)
    Q_PROPERTY(QString cancel READ cancel NOTIFY languageChange)

    Q_PROPERTY(QString sBarometricPressure READ sBarometricPressure NOTIFY languageChange)
    Q_PROPERTY(QString sAsphyxialTime READ sAsphyxialTime NOTIFY languageChange)
    Q_PROPERTY(QString sOxygenCompensation READ sOxygenCompensation NOTIFY languageChange)
    Q_PROPERTY(QString sModleName READ sModleName NOTIFY languageChange)

    Q_PROPERTY(QString sBLEEnableMes READ sBLEEnableMes NOTIFY languageChange)
    Q_PROPERTY(QString sDialogTitleMes READ sDialogTitleMes NOTIFY languageChange)
    Q_PROPERTY(QString sCloseModuleMes READ sCloseModuleMes NOTIFY languageChange)
    Q_PROPERTY(QString sConnectSuccess READ sConnectSuccess NOTIFY languageChange)
    Q_PROPERTY(QString sServerFound READ sServerFound NOTIFY languageChange)
    Q_PROPERTY(QString sAntihijackKeyErr READ sAntihijackKeyErr NOTIFY languageChange)
    Q_PROPERTY(QString sPSerror READ sPSerror NOTIFY languageChange)
    Q_PROPERTY(QString sSetAlarmParamsErr READ sSetAlarmParamsErr NOTIFY languageChange)

    Q_PROPERTY(QString sCO2ServerNotFoundErr READ sCO2ServerNotFoundErr NOTIFY languageChange)
    Q_PROPERTY(QString sCO2CharaNotFoundErr READ sCO2CharaNotFoundErr NOTIFY languageChange)
    Q_PROPERTY(QString sBLEControllerDisconnectErr READ sBLEControllerDisconnectErr NOTIFY languageChange)
    Q_PROPERTY(QString sBLEAdaptorPowerOffErr READ sBLEAdaptorPowerOffErr NOTIFY languageChange)
    Q_PROPERTY(QString sBLEReadWriteErr READ sBLEReadWriteErr NOTIFY languageChange)
    Q_PROPERTY(QString sBLEUnknownErr READ sBLEUnknownErr NOTIFY languageChange)
    Q_PROPERTY(QString sBLENoFoundErr READ sBLENoFoundErr NOTIFY languageChange)
    Q_PROPERTY(QString sDisconnectErr READ sDisconnectErr NOTIFY languageChange)

    Q_PROPERTY(QString sNoAdapterAlarm READ sNoAdapterAlarm NOTIFY languageChange)
    Q_PROPERTY(QString sAsphyxialAlarm READ sAsphyxialAlarm NOTIFY languageChange)

    Q_PROPERTY(QString sFirmwareVersion READ sFirmwareVersion NOTIFY languageChange)
    Q_PROPERTY(QString sHardwareVersion READ sHardwareVersion NOTIFY languageChange)
    Q_PROPERTY(QString sProductionDate READ sProductionDate NOTIFY languageChange)
    Q_PROPERTY(QString sSerialNumber READ sSerialNumber NOTIFY languageChange)

signals:
    void languageChange();

public:


    explicit Language(QObject *parent = 0);
//---------属性-------------
    bool isChinese();
    void setLanguage(bool language);

    //主界面
    QString sSearch() const;
    QString sSettings() const;
    QString sQuit() const;
    QString sCurrentDevice() const;
    //搜索界面
    QString  sFoundDevices() const;
    QString  sStartSearch() const;
    //设置界面
    QString  sBLEConnect() const;
    QString  sZero() const;
    QString  sAlarmParams() const;
    QString  sDisplayParams() const;
    QString  sModuleParams() const;
    QString  sPowerOff() const;
    QString  sOn() const;
    QString  sOff() const;
    QString  sUpdate() const;
    QString  sSet() const;
    QString  sLanguage() const;
    QString  sCorrect() const;
    QString  sDebug() const;
    QString  sPSInput() const;
    QString  sSystemSet() const;
    QString  sScreenWakeLock() const;
    QString  sVersion();


    //按钮
    QString OK() const;
    QString cancel() const;

    //模块参数
    QString  sBarometricPressure() const;
    QString  sAsphyxialTime() const;
    QString  sOxygenCompensation() const;
    QString  sModleName() const;

    //提示信息
    QString  sBLEEnableMes() const;
    QString  sDialogTitleMes() const;
    QString  sCloseModuleMes() const;
    QString  sConnectSuccess() const;
    QString  sServerFound() const;
    QString  sPSerror() const;
    //错误信息
    QString  sCO2ServerNotFoundErr() const;
    QString  sCO2CharaNotFoundErr() const;
    QString  sBLEControllerDisconnectErr() const;
    QString  sBLEAdaptorPowerOffErr() const;
    QString  sBLEReadWriteErr() const;
    QString  sBLEUnknownErr() const;
    QString  sBLENoFoundErr() const;
    QString  sDisconnectErr() const;
    QString  sAntihijackKeyErr() const;
    QString  sSetAlarmParamsErr() const;
    //模块错误信息
    QString sZeroNoReadyErr();
    QString sZeroingErr();
    QString sZeroDetectBreathingErr();
    //报警
    QString sNoAdapterAlarm() const;
    QString sAsphyxialAlarm() const;

    QString sFirmwareVersion() const;
    QString sHardwareVersion() const;
    QString sProductionDate() const;
    QString sSerialNumber() const;


private:
    void ChangeLanguage();
    void GetLanguageParam();
    void SetLanguageParam(QString param);

    bool m_isChinese;

    //主界面
    QString m_sSearch;
    QString m_sSettings;
    QString m_sQuit;
    QString m_sCurrentDevice;
    //搜索界面
    QString m_sFoundDevices;
    QString m_sStartSearch;
    //设置界面
    QString m_sBLEConnect;
    QString m_sZero;
    QString m_sAlarmParams;
    QString m_sDisplayParams;
    QString m_sModuleParams;
    QString m_sPowerOff;
    QString m_sOn;
    QString m_sOff;
    QString m_sUpdate;
    QString m_sSet;
    QString m_sLanguage;
    QString m_sCorrect;
    QString m_sDebug;
    QString m_sPSInput;
    QString m_sSystemSet;
    QString m_sScreenWakeLock;
    QString m_sVersion;
    QString m_sFirmwareVersion;
    QString m_sHardwareVersion;
    QString m_sProductionDate;
    QString m_sSerialNumber;

    //按钮
    QString m_OK;
    QString m_cancel;

    //模块参数
    QString m_sBarometricPressure;
    QString m_sAsphyxialTime;
    QString m_sOxygenCompensation;  
    QString m_sModleName;

    //提示信息
    QString m_sBLEEnableMes;
    QString m_sDialogTitleMes;
    QString m_sCloseModuleMes;
    QString m_sConnectSuccess;
    QString m_sServerFound;
    QString m_sPSerror;
    //错误信息
    QString m_sCO2ServerNotFoundErr;
    QString m_sCO2CharaNotFoundErr;
    QString m_sBLEControllerDisconnectErr;
    QString m_sBLEAdaptorPowerOffErr;
    QString m_sBLEReadWriteErr;
    QString m_sBLEUnknownErr;
    QString m_sBLENoFoundErr;
    QString m_sDisconnectErr;
    QString m_sAntihijackKeyErr;
    QString m_sSetAlarmParamsErr;
    //模块错误信息
    QString m_sZeroNoReadyErr;
    QString m_sZeroingErr;
    QString m_sZeroDetectBreathingErr;
    //报警
    QString m_sNoAdapterAlarm;
    QString m_sAsphyxialAlarm;

};

#endif // LANGUAGE_H
