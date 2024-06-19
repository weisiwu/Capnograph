import QtQuick 2.0
import QtQuick.Controls 2.3
import QtQuick.Extras 1.4
import QtQuick.Controls.Styles 1.4
import QtQuick.Controls 1.4


Page {
    title: qsTr(language.sSystemSet)

    Rectangle{
        //color: CapnometerSettings.backgroundColor
        anchors.fill: parent

        Column{
            anchors.fill: parent
            anchors.topMargin: CapnometerSettings.fieldMargin*0.2
            spacing:20

            Rectangle{
                //color: CapnometerSettings.backgroundColor
                height: CapnometerSettings.fieldHeight
                width: parent.width
                Text{
                    height:parent.height
                    text: language.sScreenWakeLock
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    //color:CapnometerSettings.textColor
                    anchors.left: parent.left
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    horizontalAlignment: Text.AlignVCenter
                    verticalAlignment: Text.AlignVCenter
                }
                ToggleButton{
                    id:screenlock
                    height: parent.height
                    width: CapnometerSettings.fieldWidth
                    anchors.right: parent.right
                    text: screenlock.checked ? language.sOn:language.sOff
                    checked: deviceHandler.screenLock
                    onClicked:
                        deviceHandler.screenLock=checked
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            Rectangle{
                width: parent.width
                height: CapnometerSettings.fieldHeight

                Text {
                    text: qsTr(language.sLanguage)
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    anchors.left: parent.left
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
                ExclusiveGroup{id:languageGroup}
                RadioButton{
                    id:chinese
                    exclusiveGroup: languageGroup
                    text: "中文"
                    checked: language.isChinese
                    anchors.horizontalCenter: parent.horizontalCenter
                    //anchors.horizontalCenterOffset: CapnometerSettings.fieldMargin*2
                    anchors.verticalCenter: parent.verticalCenter
                    onClicked: {
                        if(chinese.checked)
                            language.isChinese= true;
                        else
                            language.isChinese=false;

                        window.backHome();
                    }
                }

                RadioButton{
                    id:english
                    exclusiveGroup: languageGroup
                    text: "English"
                    checked: !language.isChinese
                    anchors.left: chinese.right
                    anchors.leftMargin: CapnometerSettings.fieldMargin*2
                    anchors.verticalCenter: parent.verticalCenter
                    onClicked: {
                        language.isChinese=!english.checked;
                        window.backHome();
                    }
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            Rectangle{
                width: parent.width
                height: CapnometerSettings.fieldHeight

                Text {
                    text: qsTr(language.sFirmwareVersion)
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    anchors.left: parent.left
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
                Text {
                    text: qsTr(deviceHandler.sFirmwareVersion)
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    anchors.right: parent.right
                    anchors.rightMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            Rectangle{
                width: parent.width
                height: CapnometerSettings.fieldHeight

                Text {
                    text: qsTr(language.sHardwareVersion)
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    anchors.left: parent.left
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
                Text {
                    text: qsTr(deviceHandler.sHardwareVersion)
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    anchors.right: parent.right
                    anchors.rightMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            Rectangle{
                width: parent.width
                height: CapnometerSettings.fieldHeight

                Text {
                    text: qsTr(language.sVersion)
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    anchors.left: parent.left
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
                Text {
                    text: qsTr("1.1")
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    anchors.right: parent.right
                    anchors.rightMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            Rectangle{
                width: parent.width
                height: CapnometerSettings.fieldHeight

                Text {
                    text: qsTr(language.sProductionDate)
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    anchors.left: parent.left
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
                Text {
                    text: qsTr(deviceHandler.sProductionDate)
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    anchors.right: parent.right
                    anchors.rightMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            Rectangle{
                width: parent.width
                height: CapnometerSettings.fieldHeight

                Text {
                    text: qsTr(language.sSerialNumber)
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    anchors.left: parent.left
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
                Text {
                    text: qsTr(deviceHandler.sSerialNumber)
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    anchors.right: parent.right
                    anchors.rightMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            Rectangle{
                width: parent.width
                height: CapnometerSettings.fieldHeight

                Text {
                    text: qsTr(language.sModleName)
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    anchors.left: parent.left
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
                TextField{
                    id:textServerName
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    anchors.right: parent.right
                    anchors.rightMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    horizontalAlignment: Text.left
                    verticalAlignment: Text.verticalCenter
                    //cursorVisible: true
                    text: qsTr(deviceHandler.currentDeviceName)
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            Button{
                text:language.OK
                //flat: true
                anchors.horizontalCenter: parent.horizontalCenter
                width: parent.width
                height: CapnometerSettings.fieldHeight
                onClicked: {
                    deviceHandler.renameServer(textServerName.text)
                    deviceHandler.currentDeviceName=textServerName.text
                }
            }

        }
    }

    Component.onCompleted: {
        deviceHandler.getSystemParams()
    }

}
