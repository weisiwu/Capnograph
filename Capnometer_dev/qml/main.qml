import QtQuick 2.0
import QtQuick.Controls 2.2
import QtQuick.Layouts 1.3
import QtQuick.Controls.Styles 1.4

Page {
    id: window
    visible: true
    title: qsTr("CO2 Monitor")

    Component.onCompleted: {
        CapnometerSettings.wWidth = Qt.binding(function() {return width})
        CapnometerSettings.wHeight = Qt.binding(function() {return height})
    }

    property bool backPressed: false;


    function pop()
    {
        if(stackView.depth>1)
            stackView.pop()
    }

    function push(name)
    {
        stackView.push(name)
    }

    function backHome()
    {
        while(stackView.depth>1)
            stackView.pop()
    }

    header: ToolBar {
        height: CapnometerSettings.fieldHeight

        Rectangle{
            anchors.fill: parent
            color:CapnometerSettings.viewColor
            focus: true

            RowLayout {
                anchors.fill: parent
                CapButton{
                    id: btnSearch
                    anchors.left: parent.left
                    //anchors.leftMargin: CapnometerSettings.fieldMargin*0.3
                    width:parent.width*0.17
                    height: parent.height
                    onClicked: {
                        if(stackView.depth>1){
                            stackView.pop()
                        }else{
                            stackView.push("SearchPage.qml")
                        }
                    }
                    Text{
                        anchors.centerIn: parent
                        font.pixelSize: CapnometerSettings.smallFontSize
                        text: stackView.depth > 1 ? "←" : language.sSearch
                        color:CapnometerSettings.textColor
                    }

                }
                Label {
                    text: stackView.currentItem.title
                    font.pixelSize: CapnometerSettings.mediumFontSize
                    color: CapnometerSettings.textColor
                    horizontalAlignment: Label.AlignHCenter
                    verticalAlignment: Label.AlignVCenter
                    anchors.centerIn: parent
                }

//                Rectangle{
//                    width: parent.width*0.5-btnSearch.width
//                    height: parent.height
//                    anchors.horizontalCenter: parent.horizontalCenter
//                    anchors.horizontalCenterOffset: -width*0.5
//                    color:CapnometerSettings.viewColor


//                }

//                Rectangle{
//                    width: parent.width*0.5-btnSettings.width
//                    height: parent.height
//                    anchors.horizontalCenter: parent.horizontalCenter
//                    anchors.horizontalCenterOffset: width*0.5
//                    color:CapnometerSettings.viewColor

//                    Label{
//                        color:CapnometerSettings.textColor
//                        text:"当前设备"
//                        horizontalAlignment: Label.AlignHCenter
//                        verticalAlignment: Label.AlignVCenter
//                        font.pixelSize: CapnometerSettings.smallTinyFontSize
//                        anchors.top:parent.top
//                        anchors.left: parent.left

//                    }
//                    Label{
//                        color:CapnometerSettings.textColor
//                        text: deviceHandler.currentDeviceName
//                        horizontalAlignment: Label.AlignHCenter
//                        verticalAlignment: Label.AlignVCenter
//                        font.pixelSize: CapnometerSettings.smallTinyFontSize
//                        anchors.bottom: parent.bottom
//                        anchors.left: parent.left
//                    }
//                }

                CapButton{
                    id: btnSettings
                    anchors.right: parent.right
                    //anchors.rightMargin: CapnometerSettings.fieldMargin*0.3
                    width:parent.width*0.17
                    height: parent.height
                    enabled: stackView.depth > 1 ? false:true
                    onClicked: {
                        if(stackView.depth>1){
                            stackView.pop()
                        }else{
                            stackView.push("SettingsPage.qml")
                        }
                    }
                    Text{
                        anchors.centerIn: parent
                        font.pixelSize: CapnometerSettings.smallFontSize
                        text:stackView.depth > 1 ? "" : qsTr(language.sSettings)
                        color:CapnometerSettings.textColor
                    }

                }

            }
        }
    }

    StackView {
        id: stackView
        initialItem: "MonitorPage.qml"
        anchors.fill: parent

    }

    Connections{
        target: deviceHandler
        onServerFoundChanged:{
            if(deviceHandler.serverFound){
                pop()
            }
        }
    }

    //链接过滤器信号
    Connections{
        target:keyFilter;
        onSig_KeyBackPress:{
            if (stackView.depth > 1)
            {
                stackView.pop();
            }
            else if(!backPressed)
            {
                timer.start();
                backPressed = true;
                back_animation.start();
            }
            else if(backPressed)
            {
                Qt.quit();
            }
        }
    }
    Timer{
           id:timer;
           interval: 3000;
           triggeredOnStart: false;
           onTriggered: {
                  backPressed = false;
                  timer.stop();
           }
       }

    //退出的消息框
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
               anchors.centerIn: parent;
               text:qsTr(language.sQuit);
               color: "white";
               font.pixelSize: CapnometerSettings.smallFontSize
               z:10
           }

       }
       //退出提示的动画
       SequentialAnimation{
           id:back_animation;

           NumberAnimation {
               target: exit_rect;
               property: "opacity";
               duration: 1000;
               to:100;
               easing.type: Easing.InCubic;
           }
           NumberAnimation {
               target: exit_rect;
               property: "opacity";
               duration: 1000;
               to:100;
               easing.type: Easing.InOutQuad;
           }
           NumberAnimation {
               target: exit_rect;
               property: "opacity";
               duration: 1000;
               to:0;
               easing.type: Easing.InOutQuad;
           }

       }

}
