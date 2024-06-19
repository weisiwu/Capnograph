import QtQuick 2.0
import QtQuick.Controls 2.2
import QtQuick.Dialogs 1.1
import QtCharts 2.2
import QtMultimedia 5.8

Page {
    title: qsTr("CO2 Monitor")

    property string errorMessage: deviceHandler.error
    property bool hasError: errorMessage != ""
    property bool isBLEEnable: connectionHandler.alive
    //property bool BLEalive: deviceHandler.alive
    property bool isLandScape: CapnometerSettings.wWidth>=CapnometerSettings.wHeight
    property bool alarmDisplay: false

    Rectangle{
        id:bcakGround
        anchors.fill: parent
        color:CapnometerSettings.backgroundColor

        Column {
            anchors.fill: parent
            //spacing: 8

            Rectangle{
                width: parent.width
                height: isLandScape? CapnometerSettings.fieldHeight*0.8 : CapnometerSettings.fieldHeight*0.5
                color: CapnometerSettings.backgroundColor
                anchors.left: parent.left

                Label{
                    id:curDeviceLabel
                    color:CapnometerSettings.textColor
                    text:language.sCurrentDevice
                    horizontalAlignment: Label.AlignHCenter
                    verticalAlignment: Label.AlignVCenter
                    font.pixelSize: CapnometerSettings.smallTinyFontSize
                    anchors.left: parent.left
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                }
                Label{
                    color:CapnometerSettings.textColor
                    text: deviceHandler.currentDeviceName
                    horizontalAlignment: Label.AlignHCenter
                    verticalAlignment: Label.AlignVCenter
                    font.pixelSize: CapnometerSettings.smallTinyFontSize
                    anchors.left: curDeviceLabel.right
                    anchors.verticalCenter: parent.verticalCenter
                    //anchors.bottom: parent.bottom
                }

                Label{
                    id:noAdapterAlarm
                    color:"red"
                    visible: deviceHandler.noAdapterAlarm
                    text: language.sNoAdapterAlarm
                    horizontalAlignment: Label.AlignHCenter
                    verticalAlignment: Label.AlignVCenter
                    font.pixelSize: CapnometerSettings.smallTinyFontSize
                    anchors.right: parent.right
                    anchors.verticalCenter: parent.verticalCenter
                }
                Label{
                    id:asphyxialAlarm
                    color:"red"
                    visible: deviceHandler.asphyxialAlarm
                    text: language.sAsphyxialAlarm
                    horizontalAlignment: Label.AlignHCenter
                    verticalAlignment: Label.AlignVCenter
                    font.pixelSize: CapnometerSettings.smallTinyFontSize
                    anchors.right: noAdapterAlarm.left
                    anchors.rightMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                }

                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            Rectangle{
                width: parent.width
                height: parent.height*0.382
                color:CapnometerSettings.backgroundColor

                Row{
                    anchors.fill: parent

                    ValueComponent{
                        id:etco2
                        title: qsTr("ETCO2 "+deviceHandler.sCO2Unit)
                        width: parent.width*0.5
                        height: parent.height
                        value: deviceHandler.ETCO2
                        display: alarmtimer.running ? true:true

                    }
                    ValueComponent{
                        id:rr
                        title: qsTr("RR /min")
                        width: parent.width*0.5
                        height: parent.height
                        value:deviceHandler.RespiratoryRate === 0 ? "--" : deviceHandler.RespiratoryRate
                        display: alarmtimer.running ? true:true
                    }
                }

                BottomLine{
                    height: 1
                    width: parent.width - CapnometerSettings.fieldMargin*2
                    color:"#898989"
                }
            }

            ChartView{
                id:chartView
                width: parent.width
                height: parent.height*0.618 //+ CapnometerSettings.fieldMargin *0.7
                theme: ChartView.ChartThemeDark
                backgroundColor: CapnometerSettings.backgroundColor
                animationOptions: ChartView.NoAnimation
                legend.visible: false

                ValueAxis{
                    id:axisY
                    min:0
                    max:deviceHandler.CO2Scale
                    tickCount:2
                    //labelFormat: "%.1f"
                    color:CapnometerSettings.textColor
                    lineVisible:false
                }
                ValueAxis{
                    id:axisX
                    min:0
                    max:deviceHandler.wfSpeed
                    tickCount:2
                    labelFormat: "%.0f"
                    color:CapnometerSettings.textColor
                    labelsVisible :false
                }
//                AreaSeries {
//                    id:areaSeries
//                    name:"CO2 波形"
//                    axisX: axisX
//                    axisY: axisY
//                    useOpenGL: true
//                    upperSeries:lineSeries
//                }
                LineSeries {
                    id: lineSeries
                    useOpenGL: true
                    visible: true
                    axisX: axisX
                    axisY: axisY
                    width: 5
                    color:"yellow"
                }
//                AreaSeries {
//                    axisX: axisX
//                    axisY: axisY
//                    borderColor: "black"
//                    borderWidth:8
//                    useOpenGL: true
//                    upperSeries:cursorSeries
//                }
                LineSeries {
                    id: cursorSeries
                    useOpenGL: true
                    visible: true
                    axisX: axisX
                    axisY: axisY
                    color: CapnometerSettings.backgroundColor
                    width: 5
                }
                Text{
                    anchors.right: parent.right
                    anchors.bottom: parent.bottom
                    anchors.rightMargin : isLandScape ? CapnometerSettings.fieldMargin : CapnometerSettings.fieldMargin*0.1
                    anchors.bottomMargin: isLandScape ? CapnometerSettings.fieldMargin*2 : CapnometerSettings.fieldMargin*0.7
                    width: CapnometerSettings.fieldMargin
                    height: CapnometerSettings.fieldMargin
                    text:deviceHandler.time
                    horizontalAlignment: Text.horizontalAlignment
                    verticalAlignment: Text.verticalAlignment
                    font.pixelSize: CapnometerSettings.smallTinyFontSize
                    color: CapnometerSettings.textColor
                }
            }
        }
    }

    Connections{
        target: deviceHandler
        onStatsChanged:{
            deviceHandler.update(chartView.series(0));
            deviceHandler.moveCursor(chartView.series(1));
        }
        onConnectChanged:{
            if(!deviceHandler.connectSuccess)
                warningToneTimer.running=false
        }
        onAlarmChanged:{
            if(!deviceHandler.noAdapterAlarm)
                noAdapterAlarm.visible=false
            if(!deviceHandler.asphyxialAlarm)
                asphyxialAlarm.visible=false

            if(deviceHandler.noAdapterAlarm==true || deviceHandler.asphyxialAlarm==true){
                warningToneTimer.running=false
                playAlarm.play()
                warningToneTimer.running=true
            }
            else
               warningToneTimer.running=false

        }
        onRangeAlarmChanged:{
            if(!deviceHandler.ETCO2Alarm)
                etco2.display=true;
            if(!deviceHandler.RRAlarm)
                rr.display=true;

            if(deviceHandler.ETCO2Alarm==true || deviceHandler.RRAlarm==true ){
                warningToneTimer.running=false
                playAlarm.play()
                warningToneTimer.running=true
            }
            else
               warningToneTimer.running=false
        }
    }

//    Timer{
//        id:getData
//        interval: 1/80*1000
//        repeat: true
//        running: deviceHandler.connectSuccess
//        onTriggered: {
//            deviceHandler.update(chartView.series(1));
//            deviceHandler.moveCursor(chartView.series(3));
//        }
//    }

    Timer{
        id:warningToneTimer
        interval: 20*1000
        repeat: true
        onTriggered: {
            playAlarm.play()
        }
    }

    MediaPlayer {
        id: playAlarm
        loops:0
        source: "/alarm/Medium.wav"
    }

    Timer{
        id:updateParams
        interval: 500
        running: !deviceHandler.updateParams && deviceHandler.serverFound
        repeat: true
        onTriggered: {
            deviceHandler.getSensorParams()
        }
    }

    Timer{
        id:alarmtimer
        interval: 1000
        running: (deviceHandler.ETCO2Alarm || deviceHandler.RRAlarm ||
                  deviceHandler.noAdapterAlarm || deviceHandler.asphyxialAlarm)
                 && deviceHandler.connectSuccess
        //running: false
        repeat: true
        onTriggered:{
            if(alarmDisplay)
                alarmDisplay=false
            else
                alarmDisplay=true

            if(deviceHandler.ETCO2Alarm){
                etco2.display=alarmDisplay
            }
            if(deviceHandler.RRAlarm){
                rr.display=alarmDisplay
            }
            if(deviceHandler.noAdapterAlarm){
                noAdapterAlarm.visible=alarmDisplay
            }
            if(deviceHandler.asphyxialAlarm){
                asphyxialAlarm.visible=alarmDisplay
            }

        }
    }

    MessageDialog {
        id:errorDialog
        title: language.sDialogTitleMes
        icon: StandardIcon.Critical
        text: isBLEEnable ? errorMessage : language.sBLEEnableMes
        standardButtons: StandardButton.Yes
        visible: !isBLEEnable||hasError
        onYes: {
            if(connectionHandler.alive){
                deviceHandler.error=""
                close();
            }
            else
                Qt.quit()
        }
    }
}

