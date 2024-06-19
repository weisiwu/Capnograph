import QtQuick 2.0
import QtQuick.Controls 2.3

Item {

    property string title: language.sAlarmParams

    Rectangle{
        color:CapnometerSettings.backgroundColor
        anchors.fill: parent

        Rectangle{
            id:co2Rect
            color:parent.color
            width: parent.width
            height: parent.height*0.3
            anchors.top:parent.top

            Row{
                width: parent.width
                height: parent.height*0.6
                anchors.bottom: parent.bottom
                anchors.bottomMargin: CapnometerSettings.fieldMargin
                spacing: CapnometerSettings.fieldMargin

                Text{
                    id:etco2Txt
                    text: "ETCO2"
                    width: CapnometerSettings.fieldWidth
                    color:CapnometerSettings.textColor
                    anchors.verticalCenter: parent.verticalCenter
                    anchors.left: parent.left
                    font.pixelSize: CapnometerSettings.mediumFontSize
                }

                Column{
                    anchors.left: etco2Txt.right
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    height: parent.height
                    width: parent.width*0.8
                    spacing: CapnometerSettings.fieldMargin

                    Row{
                        anchors.top:parent.top
                        anchors.left: parent.left
                        height: parent.height*0.5
                        width: parent.width
                        spacing: CapnometerSettings.fieldMargin

                        Image{
                            anchors.verticalCenter: parent.verticalCenter
                            source: "/image/upper.png"
                            fillMode: Image.PreserveAspectCrop
                            clip: true
                        }

                        CapSpinBox {
                            id: spinupperco2
                            height: CapnometerSettings.fieldHeight
                            width: parent.width*0.4
                            from:deviceHandler.etco2UpperFrom
                            value: deviceHandler.ETCO2Upper
                            stepSize:deviceHandler.etco2UpperStep
                            to:deviceHandler.etco2UpperTo
                            decimals: deviceHandler.decimal

                            onValueChanged:{
                                if(spinupperco2.value!=deviceHandler.ETCO2Upper)
                                    setButton.color=CapnometerSettings.infoColor
                            }
                        }

                        Text{
                            height: CapnometerSettings.fieldHeight
                            text: deviceHandler.sCO2Unit
                            horizontalAlignment: Text.AlignVCenter
                            verticalAlignment: Text.AlignVCenter
                            color:CapnometerSettings.textColor
                            font.pixelSize: CapnometerSettings.mediumFontSize
                        }

                    }

                    Row{
                        anchors.bottom: parent.bottom
                        anchors.left: parent.left
                        height: parent.height*0.5
                        width: parent.width
                        spacing: CapnometerSettings.fieldMargin

                        Image{
                            anchors.verticalCenter: parent.verticalCenter
                            source: "/image/lower.png"
                            fillMode: Image.PreserveAspectCrop
                            clip: true
                        }

                        CapSpinBox {
                            id:spinlowerco2
                            height: CapnometerSettings.fieldHeight
                            width: parent.width*0.4
                            from:deviceHandler.etco2LowerFrom
                            value: deviceHandler.ETCO2Lower
                            stepSize:deviceHandler.etco2LowerStep
                            //to:deviceHandler.etco2LowerTo
                            to:spinupperco2.value
                            decimals: deviceHandler.decimal

                            onValueChanged:{
                                if(spinlowerco2.value != deviceHandler.ETCO2Lower)
                                    setButton.color=CapnometerSettings.infoColor
                            }
                        }

                        Text{
                            height: CapnometerSettings.fieldHeight
                            horizontalAlignment: Text.AlignVCenter
                            verticalAlignment: Text.AlignVCenter
                            text: deviceHandler.sCO2Unit
                            color:CapnometerSettings.textColor
                            font.pixelSize: CapnometerSettings.mediumFontSize
                        }
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
            id:rrRect
            color:parent.color
            width: parent.width
            height: parent.height*0.3
            anchors.top:co2Rect.bottom

            Row{
                width: parent.width
                height: parent.height*0.6
                anchors.top:parent.top
                anchors.topMargin: CapnometerSettings.fieldMargin
                spacing: CapnometerSettings.fieldMargin

                Text{
                    id:rrTxt
                    width: CapnometerSettings.fieldWidth
                    text: "RR"
                    color:CapnometerSettings.textColor
                    anchors.verticalCenter: parent.verticalCenter
                    anchors.left: parent.left
                    font.pixelSize: CapnometerSettings.mediumFontSize
                }

                Column{
                    anchors.left: rrTxt.right
                    anchors.leftMargin: CapnometerSettings.fieldMargin
                    anchors.verticalCenter: parent.verticalCenter
                    height: parent.height
                    width: parent.width*0.8
                    spacing: CapnometerSettings.fieldMargin

                    Row{
                        anchors.top:parent.top
                        anchors.left: parent.left
                        height: parent.height*0.5
                        width: parent.width
                        spacing: CapnometerSettings.fieldMargin

                        Image{
                            anchors.verticalCenter: parent.verticalCenter
                            source: "/image/upper.png"
                            fillMode: Image.PreserveAspectCrop
                            clip: true
                        }

                        CapSpinBox {
                            id:spinupperRR
                            height: CapnometerSettings.fieldHeight
                            width: parent.width*0.4
                            from:30
                            value: deviceHandler.RRUpper
                            stepSize:10
                            to:600
                            decimals:0

                            onValueChanged:{
                                if(spinupperRR.value != deviceHandler.RRUpper)
                                     setButton.color=CapnometerSettings.infoColor
                            }
                        }

                        Text{
                            height: CapnometerSettings.fieldHeight
                            text: "bmp"
                            horizontalAlignment: Text.AlignVCenter
                            verticalAlignment: Text.AlignVCenter
                            color:CapnometerSettings.textColor
                            font.pixelSize: CapnometerSettings.mediumFontSize
                        }
                    }

                    Row{
                        anchors.bottom: parent.bottom
                        anchors.left: parent.left
                        height: parent.height*0.5
                        width: parent.width
                        spacing: CapnometerSettings.fieldMargin

                        Image{
                            anchors.verticalCenter: parent.verticalCenter
                            source: "/image/lower.png"
                            fillMode: Image.PreserveAspectCrop
                            clip: true
                        }

                        CapSpinBox {
                            id:spinlowerRR
                            height: CapnometerSettings.fieldHeight
                            width: parent.width*0.4
                            from:30
                            value: deviceHandler.RRLower
                            stepSize:10
                            //to:1000
                            to:spinupperRR.value
                            decimals:0

                            onValueChanged:{
                                if(spinlowerRR.value != deviceHandler.RRLower)
                                     setButton.color=CapnometerSettings.infoColor
                            }
                        }
                        Text{
                            height: CapnometerSettings.fieldHeight
                            horizontalAlignment: Text.AlignVCenter
                            verticalAlignment: Text.AlignVCenter
                            text: "bmp"
                            color:CapnometerSettings.textColor
                            font.pixelSize: CapnometerSettings.mediumFontSize
                        }
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
            id: updateButton
            anchors.horizontalCenter: parent.horizontalCenter
            anchors.bottom: parent.bottom
            anchors.bottomMargin: CapnometerSettings.fieldHeight*2
            width: parent.width
            height: CapnometerSettings.fieldHeight
            onClicked: deviceHandler.getAlarmParams()
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
                deviceHandler.setAlarmParams(spinupperco2.realValue,spinlowerco2.realValue,
                                                    spinupperRR.realValue,spinlowerRR.realValue,true)  
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
        deviceHandler.getAlarmParams()
    }
}
