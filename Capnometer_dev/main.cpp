#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>
#include <QtCore/QLoggingCategory>

#include <QtWidgets/QApplication>
#include <QtQml/QQmlContext>
#include <QtQuick/QQuickView>
#include <QtQml/QQmlEngine>
#include <QtCore/QDir>

#include "connectionhandler.h"
#include "devicefinder.h"
#include "devicehandler.h"
#include "keyfilter.h"
#include "language.h"

int main(int argc, char *argv[])
{
#if defined(Q_OS_WIN)
    QCoreApplication::setAttribute(Qt::AA_EnableHighDpiScaling);
#endif
/*
    QGuiApplication app(argc, argv);

    ConnectionHandler connectionHandler;
    DeviceHandler deviceHandler;
    DeviceFinder deviceFinder(&deviceHandler);

    qmlRegisterUncreatableType<DeviceHandler>("Shared", 1, 0, "AddressType", "Enum is not a type");

    QQmlApplicationEngine engine;
    engine.rootContext()->setContextProperty("connectionHandler", &connectionHandler);
    engine.rootContext()->setContextProperty("deviceFinder", &deviceFinder);
    engine.rootContext()->setContextProperty("deviceHandler", &deviceHandler);

    engine.load(QUrl(QStringLiteral("qrc:/qml/main.qml")));
    if (engine.rootObjects().isEmpty())
        return -1;

    return app.exec();

    */

    QApplication app(argc, argv);
    QQuickView viewer;

    //Q_INIT_RESOURCE(alarm);

    ConnectionHandler connectionHandler;
    DeviceHandler deviceHandler;
    DeviceFinder deviceFinder(&deviceHandler);
    KeyFilter keyFilter;
    Language language;

    qmlRegisterUncreatableType<DeviceHandler>("Shared", 1, 0, "AddressType", "Enum is not a type");
    QObject::connect(viewer.engine(), &QQmlEngine::quit, &viewer, &QWindow::close);

    viewer.setTitle(QStringLiteral("QML Oscilloscope"));

    viewer.rootContext()->setContextProperty("connectionHandler", &connectionHandler);
    viewer.rootContext()->setContextProperty("deviceFinder", &deviceFinder);
    viewer.rootContext()->setContextProperty("deviceHandler", &deviceHandler);
    viewer.rootContext()->setContextProperty("keyFilter", KeyFilter::GetInstance());
    viewer.rootContext()->setContextProperty("language",&language);



    viewer.setSource(QUrl("qrc:/qml/App.qml"));
    viewer.setResizeMode(QQuickView::SizeRootObjectToView);
    viewer.setColor(QColor("#404040"));
    viewer.show();

    //添加过滤器
    KeyFilter::GetInstance()->SetFilter(&viewer);

    return app.exec();

}
