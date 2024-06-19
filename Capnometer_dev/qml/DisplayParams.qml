import QtQuick 2.0
import QtQuick.Controls 2.3
import QtQuick.Controls.Styles 1.4
import QtQuick.Controls 1.4

Item {
    property string title: language.sDisplayParams
    property variant mmHgscale: ["50","60","75"]
    property variant kpascale: ["6.7","8","10"]
    property variant percentscale: ["6.6","7.9","9.9"]

    Rectangle{
        anchors.fill: parent
        color:CapnometerSettings.backgroundColor

        Column{
            anchors.top:parent.top
            //anchors.topMargin: CapnometerSettings.fieldMargin
            anchors.left: parent.left
            width: parent.width
            height: parent.height*0.8

            Rectangle{
                width: parent.width
                height: parent.height*0.28

                Text{
                    anchors.verticalCenter: parent.verticalCenter
                    anchors.left: parent.left
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    text: "CO2 Unit"
                    height: CapnometerSettings.fieldHeight
                    width: CapnometerSettings.fieldWidth
                    color:CapnometerSettings.backgroundColor
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    horizontalAlignment: Text.AlignVCenter
                    verticalAlignment: Text.AlignVCenter
                }

                Tumbler {
                    id:co2Unit
                    anchors.horizontalCenter: parent.horizontalCenter
                    anchors.verticalCenter: parent.verticalCenter
                    width: parent.width
                    model: ["mmHg","KPa","%"]
                    visibleItemCount: 3
                    currentIndex: deviceHandler.CO2UnitIndex
                    onCurrentIndexChanged: {
                        if(currentIndex == 0)
                            co2Scale.model=mmHgscale
                        if(currentIndex == 1)
                            co2Scale.model=kpascale
                        if(currentIndex == 2)
                            co2Scale.model=percentscale

                        if(currentIndex != deviceHandler.CO2UnitIndex){
                            setButton.color=CapnometerSettings.infoColor
                        }
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
                height: parent.height*0.28

                Text{
                    anchors.verticalCenter: parent.verticalCenter
                    anchors.left: parent.left
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    text: "CO2 Scale"
                    height: CapnometerSettings.fieldHeight
                    width: CapnometerSettings.fieldWidth
                    color:CapnometerSettings.backgroundColor
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    horizontalAlignment: Text.AlignVCenter
                    verticalAlignment: Text.AlignVCenter
                }

                Tumbler {
                    id:co2Scale
                    anchors.horizontalCenter: parent.horizontalCenter
                    anchors.verticalCenter: parent.verticalCenter
                    width: parent.width
                    model: mmHgscale
                    visibleItemCount: 3
                    currentIndex: deviceHandler.CO2ScaleIndex

                    onCurrentIndexChanged: {
                        if(currentIndex != deviceHandler.CO2ScaleIndex){
                            setButton.color=CapnometerSettings.infoColor
                        }
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
                height: parent.height*0.28

                Text{
                    anchors.verticalCenter: parent.verticalCenter
                    anchors.left: parent.left
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    text: "WF.Speed"
                    height: CapnometerSettings.fieldHeight
                    width: CapnometerSettings.fieldWidth
                    color:CapnometerSettings.backgroundColor
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    horizontalAlignment: Text.AlignVCenter
                    verticalAlignment: Text.AlignVCenter
                }

                Tumbler {
                    id:wfSpeed
                    anchors.horizontalCenter: parent.horizontalCenter
                    anchors.verticalCenter: parent.verticalCenter
                    width: parent.width
                    model: ["4mm/s","2mm/s","1mm/s"]
                    currentIndex: deviceHandler.wfSpeedIndex
                    visibleItemCount: 3

                    onCurrentIndexChanged: {
                        if(currentIndex != deviceHandler.wfSpeedIndex){
                            setButton.color=CapnometerSettings.infoColor
                        }
                    }
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }

        }

        CapButton{
            id: updateButton
            anchors.horizontalCenter: parent.horizontalCenter
            anchors.bottom: parent.bottom
            anchors.bottomMargin: CapnometerSettings.fieldHeight*2
            width: parent.width
            height: CapnometerSettings.fieldHeight
            onClicked: {
                deviceHandler.updateCO2Unit()
                deviceHandler.updateCO2Scale()
                deviceHandler.updatewfSpeed()
            }

            Text {
                anchors.centerIn: parent
                font.pixelSize: CapnometerSettings.tinyFontSize
                text: qsTr(language.sUpdate)
                color:  CapnometerSettings.textColor
            }

        }
        CapButton{
            id: setButton
            anchors.horizontalCenter: parent.horizontalCenter
            anchors.bottom: parent.bottom
            anchors.bottomMargin: CapnometerSettings.fieldMargin
            width: parent.width
            height: CapnometerSettings.fieldHeight
            onClicked: {
                deviceHandler.setDisplayParams(co2Unit.currentIndex,co2Scale.currentIndex,wfSpeed.currentIndex)
                //window.pop()
            }

            Text {
                anchors.centerIn: parent
                font.pixelSize: CapnometerSettings.tinyFontSize
                text: qsTr(language.sSet)
                color:  CapnometerSettings.textColor
            }

        }

    }
    Component.onCompleted: {
        deviceHandler.updateCO2Unit()
        deviceHandler.updateCO2Scale()
        deviceHandler.updatewfSpeed()
    }

    Connections{
        target: deviceHandler
        onDisplayParamChanged:{
            if(deviceHandler.CO2UnitIndex == 0)
                co2Scale.model=mmHgscale
            if(deviceHandler.CO2UnitIndex == 1)
                co2Scale.model=kpascale
            if(deviceHandler.CO2UnitIndex == 2)
                co2Scale.model=percentscale
        }
    }
}
