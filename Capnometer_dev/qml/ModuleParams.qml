import QtQuick 2.0
import QtQuick.Controls 2.3

Item {

    property string title: language.sModuleParams

    Rectangle{
        color: CapnometerSettings.backgroundColor
        anchors.fill: parent

        Column{
            width: parent.width
            height: parent.height*0.8
            spacing:5

            SettingsLable{
                id:barometricPressure
                enabled: false
                title: language.sBarometricPressure+"(mmHg)"
                from: 400
                to:850
                value: deviceHandler.barometricPressure
                stepSize: 1

                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }
            SettingsLable{
                id:noBreaths
                title: language.sAsphyxialTime+"(S)"
                from: 10
                to:60
                value: deviceHandler.NoBreaths
                stepSize: 1
                onSliderMoved : {
                    if(value != deviceHandler.NoBreaths)
                        setButton.color=CapnometerSettings.infoColor
                }

                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }
            SettingsLable{
                id:o2Compensation
                title: language.sOxygenCompensation+"(%)"
                from: 0
                to:100
                value: deviceHandler.O2Compensation
                stepSize: 1
                onSliderMoved : {
                    if(value != deviceHandler.O2Compensation)
                        setButton.color=CapnometerSettings.infoColor
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
                deviceHandler.updateBarometricPressure()
                deviceHandler.updateNoBreaths()
                deviceHandler.updateO2Compensation()
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
                deviceHandler.setSensorParams(barometricPressure.value,
                                              noBreaths.value,o2Compensation.value)
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
        deviceHandler.updateBarometricPressure()
        deviceHandler.updateNoBreaths()
        deviceHandler.updateO2Compensation()
    }
}
