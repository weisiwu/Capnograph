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
        NavigationView() {
            VStack(spacing: 0) {
                if !bluetoothManager.discoveredPeripherals.isEmpty {
                    List(bluetoothManager.discoveredPeripherals, id: \.identifier) { peripheral in
                        HStack {
                            VStack(alignment: .leading) {
                                Text(peripheral.name ?? "MainUnknownName")
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
                                Text("SearchConfirmYes")
                                    .frame(width: 68, height: 32)
                                    .font(.system(size: 16))
                                    .fontWeight(.thin)
                                    .background(Color(red: 232/255, green: 243/255, blue: 1))
                                    .foregroundColor(Color(red: 22/255, green: 93/255, blue: 1))
                                    .cornerRadius(16)
                                    .alert(isPresented: $showAlert) {
                                        Alert(
                                            title: Text("MainUnknownName"),
                                            message: Text("\("SearchDevicePrefix"): \(selectedPeripheral?.name ?? "SearchConfirmTitle")"),
                                            primaryButton: .default(Text("SearchConfirmYes"), action: {
                                                appConfigManage.loadingMessage = "SearchConnecting"
                                                bluetoothManager.connect(to: selectedPeripheral) {
                                                    withAnimation {
                                                        appConfigManage.loadingMessage = ""
                                                        appConfigManage.toastMessage = "SearchConnected"
                                                        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                                            selectedTabIndex = PageTypes.Result.rawValue
                                                            appConfigManage.toastMessage = ""
                                                        }
                                                    }
                                                }
                                            }),
                                            secondaryButton: .default(Text("SearchConfirmNo"))
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
                Text("SearchBtn")
                    .frame(width: 105, height: 35)
                    .font(.system(size: 16))
                    .background(Color(red: 232/255, green: 243/255, blue: 1))
                    .foregroundColor(Color(red: 22/255, green: 93/255, blue: 1))
                    .cornerRadius(16)
                    .padding(.bottom, 32)
                    .onTapGesture {
                        appConfigManage.loadingMessage = "SearchSearching"
                        // 开启搜索后，会不停的搜搜外设
                        bluetoothManager.startScanning() {
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                appConfigManage.loadingMessage = ""
                            }
                        }
                    }
            }
            .navigationTitle("TitleSearch")
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarBackButtonHidden(false)
        }
    }
}
