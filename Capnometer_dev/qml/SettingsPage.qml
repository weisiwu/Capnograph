import QtQuick 2.0
import QtQuick.Controls 2.3
import QtQuick.Controls.Styles 1.4
import QtQuick.Extras 1.4
import QtQuick.Controls 1.4
import QtQuick.Dialogs 1.1

Page {
    title: qsTr(language.sSettings)

    Rectangle{
        color: CapnometerSettings.backgroundColor
        anchors.fill: parent

        Column{
            anchors.fill: parent
            spacing:5

            Rectangle{
                color: CapnometerSettings.backgroundColor
                height: CapnometerSettings.fieldHeight
                width: parent.width
                Text{
                    height:parent.height
                    text: language.sBLEConnect
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    color:CapnometerSettings.textColor
                    anchors.left: parent.left
                    horizontalAlignment: Text.AlignVCenter
                    verticalAlignment: Text.AlignVCenter
                }
                ToggleButton{
                    id:blueTooth
                    height: parent.height
                    width: CapnometerSettings.fieldWidth
                    anchors.right: parent.right
                    text: blueTooth.checked ? language.sOn:language.sOff
                    checked: deviceHandler.serverFound
                    onClicked: {
                        if(!blueTooth.checked)
                            deviceHandler.disconnectService()
                        else if(blueTooth.checked){
                            deviceFinder.startSearch()
                            window.pop()
                        }

                    }
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            CapButton{
                id: zeroButton
                color:CapnometerSettings.backgroundColor
                height: CapnometerSettings.fieldHeight
                width: parent.width
                onClicked: {
                    if(deviceHandler.serverFound){
                        deviceHandler.writeZeroValue()
                    }
                }

                Text{
                    id:textzero
                    height:parent.height
                    text: language.sZero
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    color:CapnometerSettings.textColor
                    anchors.left: parent.left
                    horizontalAlignment: Text.AlignVCenter
                    verticalAlignment: Text.AlignVCenter
                }
                Rectangle{
                    //anchors.left: textzero.right
                    height: parent.height
                    width: count/maxCount*(parent.width)
                    color:CapnometerSettings.infoColor

                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            CapButton{
                id: alarmButton
                color:CapnometerSettings.backgroundColor
                height: CapnometerSettings.fieldHeight
                width: parent.width
                onClicked: window.push("AlarmParams.qml")

                Text{
                    height:parent.height
                    text: language.sAlarmParams
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    color:CapnometerSettings.textColor
                    anchors.left: parent.left
                    horizontalAlignment: Text.AlignVCenter
                    verticalAlignment: Text.AlignVCenter
                }
                Text{
                    height:parent.height
                    text: ">"
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    color:CapnometerSettings.textColor
                    anchors.right: parent.right
                    anchors.rightMargin: CapnometerSettings.fieldMargin
                    verticalAlignment: Text.AlignVCenter
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            CapButton{
                id:disPlayButton
                color:CapnometerSettings.backgroundColor
                height: CapnometerSettings.fieldHeight
                width: parent.width
                onClicked: window.push("DisplayParams.qml")

                Text{
                    height:parent.height
                    text: language.sDisplayParams
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    color:CapnometerSettings.textColor
                    anchors.left: parent.left
                    horizontalAlignment: Text.AlignVCenter
                    verticalAlignment: Text.AlignVCenter
                }
                Text{
                    height:parent.height
                    text: ">"
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    color:CapnometerSettings.textColor
                    anchors.rightMargin: CapnometerSettings.fieldMargin
                    anchors.right: parent.right
                    verticalAlignment: Text.AlignVCenter
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            CapButton{
                id:moduleButton
                color:CapnometerSettings.backgroundColor
                height: CapnometerSettings.fieldHeight
                width: parent.width
                onClicked: window.push("ModuleParams.qml")

                Text{
                    height:parent.height
                    text: language.sModuleParams
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    color:CapnometerSettings.textColor
                    anchors.left: parent.left
                    horizontalAlignment: Text.AlignVCenter
                    verticalAlignment: Text.AlignVCenter
                }
                Text{
                    height:parent.height
                    text: ">"
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    color:CapnometerSettings.textColor
                    anchors.right: parent.right
                    anchors.rightMargin: CapnometerSettings.fieldMargin
                    verticalAlignment: Text.AlignVCenter
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }
            CapButton{
                id:debugButton
                color:CapnometerSettings.backgroundColor
                height: CapnometerSettings.fieldHeight
                width: parent.width
                onClicked: window.push("Debug.qml")

                Text{
                    height:parent.height
                    text: language.sDebug
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    color:CapnometerSettings.textColor
                    anchors.left: parent.left
                    horizontalAlignment: Text.AlignVCenter
                    verticalAlignment: Text.AlignVCenter
                }
                Text{
                    height:parent.height
                    text: ">"
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    color:CapnometerSettings.textColor
                    anchors.right: parent.right
                    anchors.rightMargin: CapnometerSettings.fieldMargin
                    verticalAlignment: Text.AlignVCenter
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }
            CapButton{
                id: systemButton
                color:CapnometerSettings.backgroundColor
                height: CapnometerSettings.fieldHeight
                width: parent.width
                onClicked: window.push("SystemSettings.qml")

                Text{
                    height:parent.height
                    text: language.sSystemSet
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    color:CapnometerSettings.textColor
                    anchors.left: parent.left
                    horizontalAlignment: Text.AlignVCenter
                    verticalAlignment: Text.AlignVCenter
                }
                Text{
                    height:parent.height
                    text: ">"
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    color:CapnometerSettings.textColor
                    anchors.right: parent.right
                    anchors.rightMargin: CapnometerSettings.fieldMargin
                    verticalAlignment: Text.AlignVCenter
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            CapButton{
                height: CapnometerSettings.fieldHeight
                width: parent.width
                onClicked: {
                    isCloseModule=true;

                }

                Text {
                    text: qsTr(language.sPowerOff)
                    color:"red"
                    anchors.horizontalCenter: parent.horizontalCenter
                    anchors.verticalCenter: parent.verticalCenter
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
            }

        }
    }

    Connections{
        target: deviceHandler
        onZeroStatusChanged:{
            zeroTimer.running=deviceHandler.canZero

        }
    }

    property int count: 0
    property int maxCount: 10
    Timer {
        id: zeroTimer
        interval: 1000
        repeat: true
        onTriggered: {
            if(count>=maxCount){
                count=0
                zeroTimer.running=false
                zeroButton.enabled=true
                window.pop()
            }
            else{
                count++
                zeroButton.enabled=false
            }
        }
    }

    property bool isCloseModule: false

    MessageDialog {
        title: language.sDialogTitleMes
        icon: StandardIcon.Question
        text: qsTr(language.sCloseModuleMes)
        standardButtons:StandardButton.Yes|StandardButton.No
        visible: isCloseModule
        onYes:{
            deviceHandler.shutDown()
            isCloseModule=false;
        }
        onNo: {
            close();
            isCloseModule=false;
        }

    }

}
