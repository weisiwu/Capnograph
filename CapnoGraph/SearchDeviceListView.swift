import SwiftUI
import CoreBluetooth

struct SearchDeviceListView: View {
    @State private var showAlert = false
    @State public var selectedPeripheral: CBPeripheral? = nil
    @Binding var showToast: Bool
    @Binding var selectedTabIndex: Int
    @EnvironmentObject var bluetoothManager: BluetoothManager
    let systemHeight:CGFloat = UIScreen.main.bounds.height - 200
    var toggleLoading: ((Bool, String) -> Bool)?
    
    var body: some View {
        NavigationView() {
            VStack(spacing: 0) {
                if !bluetoothManager.discoveredPeripherals.isEmpty {
                    List(bluetoothManager.discoveredPeripherals, id: \.identifier) { peripheral in
                        HStack {
                            VStack(alignment: .leading) {
                                Text(peripheral.name ?? "未知设备")
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
                                Text("链接")
                                    .frame(width: 68, height: 32)
                                    .font(.system(size: 16))
                                    .fontWeight(.thin)
                                    .background(Color(red: 232/255, green: 243/255, blue: 1))
                                    .foregroundColor(Color(red: 22/255, green: 93/255, blue: 1))
                                    .cornerRadius(16)
                                    .alert(isPresented: $showAlert) {
                                        Alert(
                                            title: Text("确认要链接此设备？"),
                                            // TODO: 这里的值文案不对
                                            message: Text("设备名: \(selectedPeripheral?.name ?? "未知设备")"),
                                            primaryButton: .default(Text("链接"), action: {
                                                if let toggleLoading {
                                                    toggleLoading(true, "链接中")
                                                    bluetoothManager.connect(to: selectedPeripheral) {
                                                        withAnimation {
                                                            toggleLoading(false, "")
                                                            showToast = true
                                                            bluetoothManager.toastMessage = "链接成功"
                                                            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                                                showToast = false
                                                                selectedTabIndex = PageTypes.Result.rawValue
                                                            }
                                                        }
                                                    }
                                                }
                                            }),
                                            secondaryButton: .default(Text("取消"))
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
                Text("搜索设备")
                    .frame(width: 105, height: 35)
                    .font(.system(size: 16))
                    .background(Color(red: 232/255, green: 243/255, blue: 1))
                    .foregroundColor(Color(red: 22/255, green: 93/255, blue: 1))
                    .cornerRadius(16)
                    .padding(.bottom, 32)
                    .onTapGesture {
                        if let toggleLoading {
                            let isSearch = toggleLoading(true, "搜索设备中")
                            // 开启搜索后，会不停的搜搜外设
                            bluetoothManager.startScanning() {
                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                    toggleLoading(false, "")
                                }
                            }
                        }
                    }
            }
            .navigationTitle("CapnoGraph - 附近设备")
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarBackButtonHidden(false)
        }
    }
}


//#Preview {
//    VStack {
//        Text("测试")
//            .frame(height: 500)
//        // TODO: 测试成功效果
//        Toast(message: "成功")
//    }
//}
//#Preview {
//    SearchDeviceListView()
//}
