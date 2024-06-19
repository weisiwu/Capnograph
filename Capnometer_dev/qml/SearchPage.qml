import QtQuick 2.0
import QtQuick.Controls 2.2
import Shared 1.0
import QtQuick.Dialogs 1.1

Page {
    id:searchPage
    title: qsTr(language.sSearch)

    property bool blistEnable: true

    Rectangle{
        id:pageBackGround
        anchors.fill:parent
        color:CapnometerSettings.backgroundColor

        Rectangle{
            id:info
            anchors.top:parent.top
            height: CapnometerSettings.fieldHeight*0.5
            width: parent.width
            color:CapnometerSettings.backgroundColor

            //            InfoPage{
            //                errorMessage: deviceFinder.error
            //                infoMessage: deviceFinder.info
            //            }
        }

        Rectangle{
            id:listContainer
            anchors.top:info.bottom
            //anchors.topMargin: CapnometerSettings.fieldMargin + messageHeight
            anchors.bottom: searchButton.top
            anchors.bottomMargin: CapnometerSettings.fieldMargin
            anchors.horizontalCenter: parent.horizontalCenter
            width: parent.width-CapnometerSettings.fieldMargin*2
            color: CapnometerSettings.viewColor
            radius: CapnometerSettings.buttonRadius

            Text {
                id: title
                text: qsTr(language.sFoundDevices)
                width: parent.width
                height: CapnometerSettings.fieldHeight
                horizontalAlignment: Text.AlignHCenter
                verticalAlignment: Text.AlignVCenter
                color:searchButton.enabled ? CapnometerSettings.textColor : CapnometerSettings.disabledTextColor
                font.pixelSize: CapnometerSettings.mediumFontSize

                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

            ListView{
                id:devices
                anchors.left: parent.left
                anchors.right: parent.right
                anchors.bottom: parent.bottom
                anchors.top:title.bottom
                model: deviceFinder.devices
                clip: true
                enabled:blistEnable

                delegate: Rectangle{
                    id:box
                    height: CapnometerSettings.fieldHeight*1.2
                    width: parent.width
                    color: index % 2 === 0 ? CapnometerSettings.delegate1Color : CapnometerSettings.delegate2Color

                    MouseArea{
                        anchors.fill: parent
                        onClicked: {
                            deviceFinder.connectToService(modelData.deviceAddress);
                            blistEnable=false;
                            up_animation.start();
                        }
                        onPressed: {
                            box.color=CapnometerSettings.disabledButtonColor;
                            devices.currentIndex=index;
                        }
                        onReleased: {
                            if(devices.currentIndex % 2 == 0)
                                box.color=CapnometerSettings.delegate1Color
                            else
                                box.color=CapnometerSettings.delegate2Color
                        }
                    }

                    Text {
                        id: device
                        font.pixelSize: CapnometerSettings.smallFontSize
                        text: modelData.deviceName
                        anchors.top: parent.top
                        anchors.topMargin: parent.height * 0.1
                        anchors.leftMargin: parent.height * 0.1
                        anchors.left: parent.left
                        color: CapnometerSettings.textColor
                    }

                    Text {
                        id: deviceAddress
                        font.pixelSize: CapnometerSettings.smallFontSize
                        text: modelData.deviceAddress
                        anchors.bottom: parent.bottom
                        anchors.bottomMargin: parent.height * 0.1
                        anchors.rightMargin: parent.height * 0.1
                        anchors.right: parent.right
                        color: Qt.darker(CapnometerSettings.textColor)
                    }
                }
            }
        }

        CapButton{
            id: searchButton
            anchors.horizontalCenter: parent.horizontalCenter
            anchors.bottom: parent.bottom
            anchors.bottomMargin: CapnometerSettings.fieldMargin
            width: listContainer.width
            height: CapnometerSettings.fieldHeight
            enabled: !deviceFinder.scanning
            onClicked: {
                blistEnable=true;
                deviceFinder.startSearch();
                down_animation.start();
            }

            Text {
                anchors.centerIn: parent
                font.pixelSize: CapnometerSettings.tinyFontSize
                text: qsTr(language.sStartSearch)
                color: searchButton.enabled ? CapnometerSettings.textColor : CapnometerSettings.disabledTextColor
            }

        }

    }

//    MessageDialog {
//        id:errDialog
//        title: language.sDialogTitleMes
//        icon: StandardIcon.Critical
//        text: deviceFinder.error
//        standardButtons: StandardButton.Yes
//        visible: deviceFinder.error !=""
//        onYes: {
//            close()
//        }
//    }

    Connections{
        target: deviceHandler
        onServerFoundChanged:{
            down_animation.start()
        }
    }
    Connections{
        target: deviceHandler
        onHideMessage:{
            blistEnable=true;
            down_animation.start()
        }
    }

    Rectangle{
        id:exit_rect;
        radius: CapnometerSettings.buttonRadius
        z: 10
        width: parent.width*0.8
        height: CapnometerSettings.fieldHeight*0.9
        color:"gray"
        opacity:0;
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.verticalCenter: parent.verticalCenter

        Text{
            id:hite
            anchors.centerIn: parent
            text: deviceHandler.connectSuccess ? language.sConnectSuccess : ""
            color: "white"
            font.pixelSize: CapnometerSettings.smallFontSize
            z:10
        }
        BusyIndicator{
            anchors.horizontalCenter: parent.horizontalCenter
            anchors.horizontalCenterOffset: deviceHandler.connectSuccess ? hite.width*0.5+hite.height : 0
            width: parent.width
            height: parent.height
            running: !blistEnable
        }
    }

    SequentialAnimation{
        id:up_animation;

        NumberAnimation {
            target: exit_rect;
            property: "opacity";
            duration: 500;
            to:100;
            easing.type: Easing.InCubic;
        }
    }

    SequentialAnimation{
        id:down_animation;

        NumberAnimation {
            target: exit_rect;
            property: "opacity";
            duration: 100;
            to:0;
            easing.type: Easing.InOutQuad;
        }

    }

}
