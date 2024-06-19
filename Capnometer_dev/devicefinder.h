#ifndef DEVICEFINDER_H
#define DEVICEFINDER_H

#include "bluetoothbaseclass.h"

#include <QTimer>
#include <QBluetoothDeviceDiscoveryAgent>
#include <QBluetoothDeviceInfo>
#include <QVariant>

class DeviceInfo;
class DeviceHandler;

class DeviceFinder: public BluetoothBaseClass
{
    Q_OBJECT

    Q_PROPERTY(bool scanning READ scanning NOTIFY scanningChanged)
    Q_PROPERTY(QVariant devices READ devices NOTIFY devicesChanged)

public:
    DeviceFinder(DeviceHandler *handler, QObject *parent = 0);
    ~DeviceFinder();

    bool scanning() const;
    QVariant devices();

public slots:
    void startSearch();
    void connectToService(const QString &address);

private slots:
    void addDevice(const QBluetoothDeviceInfo&);
    void scanError(QBluetoothDeviceDiscoveryAgent::Error error);
    void scanFinished();
    QString GetUsedDevice();
    void SetUsedDevice(QString address);

signals:
    void scanningChanged();
    void devicesChanged();

private:
    DeviceHandler *m_deviceHandler;
    QBluetoothDeviceDiscoveryAgent *m_deviceDiscoveryAgent;
    QList<QObject*> m_devices;




};

#endif // DEVICEFINDER_H
