import QtQuick 2.0
import QtQuick.Controls 2.3

Page {
    title: qsTr(language.sDebug)

    Rectangle{
        color: CapnometerSettings.backgroundColor
        anchors.fill: parent

        Column{
            anchors.fill: parent
            anchors.topMargin: CapnometerSettings.fieldMargin*0.2
            spacing:20

            CapButton{
                color: CapnometerSettings.backgroundColor
                height: CapnometerSettings.fieldHeight
                width: parent.width
                onClicked: {
                    deviceHandler.writeCorrectValue();
                }

                Text {
                    text: qsTr(language.sCorrect)
                    color:CapnometerSettings.textColor
                    anchors.horizontalCenter: parent.horizontalCenter
                    anchors.verticalCenter: parent.verticalCenter
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    horizontalAlignment: Text.horizontalCenter
                    verticalAlignment: Text.verticalCenter
                }
                BottomLine{
                    height: 1
                    width: parent.width
                    color:"#898989"
                }
            }
        }
    }



}
