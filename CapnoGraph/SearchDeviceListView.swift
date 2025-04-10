import SwiftUI
import CoreBluetooth

struct SearchDeviceListView: View {
    @State private var showAlert = false
    @State public var selectedPeripheral: CBPeripheral? = nil
    @Binding var selectedTabIndex: Int
    @EnvironmentObject var bluetoothManager: BluetoothManager
    @EnvironmentObject var appConfigManage: AppConfigManage
    let systemHeight:CGFloat = UIScreen.main.bounds.height - 200
    
    var body: some View {
        NavigationStack() {
            VStack(spacing: 0) {
                if !bluetoothManager.discoveredPeripherals.isEmpty {
                    List(bluetoothManager.discoveredPeripherals, id: \.identifier) { peripheral in
                        HStack {
                            VStack(alignment: .leading) {
                                Text(peripheral.name ?? appConfigManage.getTextByKey(key: "MainUnknownName"))
                                    .font(.system(size: 16))
                                    .padding(.bottom, 0)
                                Text(peripheral.identifier.uuidString)
                                    .font(.system(size: 15))
                                    .fontWeight(.thin)
                                    .foregroundColor(Color(red: 136/255, green: 136/255, blue: 136/255))
                            }
                            Spacer()
                            Button(action: {
                                selectedPeripheral = peripheral
                                showAlert = true
                            }) {
                                Text(appConfigManage.getTextByKey(key: "SearchConfirmYes"))
                                    .frame(width: 68, height: 32)
                                    .font(.system(size: 16))
                                    .fontWeight(.thin)
                                    .background(Color(red: 232/255, green: 243/255, blue: 1))
                                    .foregroundColor(Color(red: 22/255, green: 93/255, blue: 1))
                                    .cornerRadius(16)
                            }
                        }
                        .frame(maxHeight: .infinity)
                    }
                    .background(Color.white)
                    .listStyle(PlainListStyle())
                    .padding(.bottom, 48)
                    .alert(
                        Text(appConfigManage.getTextByKey(key: "MainUnknownName")),
                        isPresented: $showAlert
                    ) {
                        Button(appConfigManage.getTextByKey(key: "SearchConfirmYes")) {
                            appConfigManage.loadingMessage = appConfigManage.getTextByKey(key: "SearchConnecting")
                            bluetoothManager.connect(to: selectedPeripheral) { isConnectSuccess in
                                withAnimation {
                                    appConfigManage.loadingMessage = ""
                                    appConfigManage.toastMessage = appConfigManage.getTextByKey(key: "SearchConnected")
                                    appConfigManage.toastType = .SUCCESS
                                    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                        selectedTabIndex = PageTypes.Result.rawValue
                                        appConfigManage.toastMessage = ""
                                    }
                                }
                            }
                        }
                        Button(appConfigManage.getTextByKey(key: "SearchConfirmNo")) {
                            showAlert = false
                        }
                    } message: {
                        Text("\(appConfigManage.getTextByKey(key: "SearchDevicePrefix")): \(selectedPeripheral?.name ?? appConfigManage.getTextByKey(key: "SearchConfirmTitle"))")
                    }
                } else {
                        Image("device_empty_list")
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(width: 500, height: 420)
                    GeometryReader { geometry in
                        Text(appConfigManage.getTextByKey(key: "SearchNoResult"))
                            .font(.system(size: 20))
                            .foregroundColor(Color(red: 104/255, green: 115/255, blue: 113/255))
                            .position(x: geometry.size.width / 2, y: -100)
                    }
                }
                Text(appConfigManage.getTextByKey(key: "SearchBtn"))
                    .frame(width: 120, height: 40)
                    .font(.system(size: 16))
                    .background(Color(red: 232/255, green: 243/255, blue: 1))
                    .foregroundColor(Color(red: 22/255, green: 93/255, blue: 1))
                    .cornerRadius(20)
                    .padding(.bottom, 32)
                    .onTapGesture {
                        appConfigManage.loadingMessage = appConfigManage.getTextByKey(key: "SearchSearching")
                        // 开启搜索后，会不停的搜索外设
                        bluetoothManager.startScanning() {
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                appConfigManage.loadingMessage = ""
                                if bluetoothManager.discoveredPeripherals.count > 0 {
                                    bluetoothManager.isScanning = false
                                    return
                                }
                                bluetoothManager.isScanning = false
                                appConfigManage.toastMessage = appConfigManage.getTextByKey(key: "SearchFail")
                                DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                    appConfigManage.toastMessage = ""
                                }
                            }
                        }
                    }
            }
            .navigationTitle("CapnoGraph\(appConfigManage.getTextByKey(key: "TitleSearch"))")
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarBackButtonHidden(false)
        }
    }
}
