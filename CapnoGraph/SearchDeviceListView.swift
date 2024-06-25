import SwiftUI
import CoreBluetooth

struct SearchDeviceListView: View {
    @State private var showAlert = false
    @State public var selectedPeripheral: CBPeripheral? = nil
    @Binding var showToast: Bool
    @Binding var selectedTabIndex: Int
    @EnvironmentObject var bluetoothManager: BluetoothManager
    @EnvironmentObject var appConfigManage: AppConfigManage
    let systemHeight:CGFloat = UIScreen.main.bounds.height - 200
    var toggleLoading: ((Bool, String) -> Bool)?
    
    var body: some View {
        NavigationView() {
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
                                    .alert(isPresented: $showAlert) {
                                        Alert(
                                            title: Text(appConfigManage.getTextByKey(key: "MainUnknownName")),
                                            message: Text("\(appConfigManage.getTextByKey(key: "SearchDevicePrefix")): \(selectedPeripheral?.name ?? appConfigManage.getTextByKey(key: "SearchConfirmTitle"))"),
                                            primaryButton: .default(Text(appConfigManage.getTextByKey(key: "SearchConfirmYes")), action: {
                                                if let toggleLoading {
                                                    toggleLoading(true, appConfigManage.getTextByKey(key: "SearchConnected"))
                                                    bluetoothManager.connect(to: selectedPeripheral) {
                                                        withAnimation {
                                                            toggleLoading(false, "")
                                                            showToast = true
                                                            bluetoothManager.toastMessage = appConfigManage.getTextByKey(key: "SearchConnecting")
                                                            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                                                showToast = false
                                                                selectedTabIndex = PageTypes.Result.rawValue
                                                            }
                                                        }
                                                    }
                                                }
                                            }),
                                            secondaryButton: .default(Text(appConfigManage.getTextByKey(key: "SearchConfirmNo")))
                                        )
                                    }
                            }
                        }
                        .frame(maxHeight: .infinity)
                    }
                    .background(Color.white)
                    .listStyle(PlainListStyle())
                    .padding(.bottom, 48)
                } else {
                    Image("device_empty_list")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 500)
                        .padding(.bottom, 48)
                }
                // TODO: 这里的按钮没有固定里底部距离
                Text(appConfigManage.getTextByKey(key: "SearchBtn"))
                    .frame(width: 105, height: 35)
                    .font(.system(size: 16))
                    .background(Color(red: 232/255, green: 243/255, blue: 1))
                    .foregroundColor(Color(red: 22/255, green: 93/255, blue: 1))
                    .cornerRadius(16)
                    .padding(.bottom, 32)
                    .onTapGesture {
                        if let toggleLoading {
                            let isSearch = toggleLoading(true, appConfigManage.getTextByKey(key: "SearchSearching"))
                            // 开启搜索后，会不停的搜搜外设
                            bluetoothManager.startScanning() {
                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                    toggleLoading(false, "")
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

//#Preview {
//    SearchDeviceListView()
//}
