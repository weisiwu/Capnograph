import SwiftUI

struct DeviceInfo: Identifiable {
    let id = UUID()
    let title: String
    let macAddress: String
}

struct SearchDeviceListView: View {
    @State private var devicesListData: [DeviceInfo]? = nil
    @State private var showAlert = false
    @Binding var showToast: Bool
    let systemHeight:CGFloat = UIScreen.main.bounds.height - 200
    var toggleLoading: ((Bool, String) -> Bool)?
    
    var body: some View {
        VStack(spacing: 0) {
            if let devicesListData {
                List(devicesListData) { item in
                    HStack {
                        VStack(alignment: .leading) {
                            Text(item.title)
                                .font(.system(size: 17))
                                .padding(.bottom, 2)
                            Text(item.macAddress)
                                .font(.system(size: 15))
                                .fontWeight(.thin)
                                .foregroundColor(Color(red: 136/255, green: 136/255, blue: 136/255))
                        }
                        Spacer()
                        Button(action: {
                            showAlert = true
                        }) {
                            Text("链接")
                                .frame(width: 68, height: 32)
                                .font(.system(size: 18))
                                .fontWeight(.thin)
                                .background(Color(red: 232/255, green: 243/255, blue: 1))
                                .foregroundColor(Color(red: 22/255, green: 93/255, blue: 1))
                                .cornerRadius(16)
                                .alert(isPresented: $showAlert) {
                                    Alert(
                                        title: Text("确认要链接此设备？"),
                                        message: Text("设备名: \(item.title)"),
                                        primaryButton: .default(Text("链接"), action: {
                                            if let toggleLoading {
                                                let isConnecting = toggleLoading(true, "链接中")
                                                if isConnecting {
                                                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                                                        toggleLoading(false, "")
                                                        withAnimation {
                                                            showToast = true
                                                            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                                                withAnimation {
                                                                    showToast = false
                                                                }
                                                            }
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
                .font(.system(size: 18))
                .background(Color(red: 232/255, green: 243/255, blue: 1))
                .foregroundColor(Color(red: 22/255, green: 93/255, blue: 1))
                .cornerRadius(16)
                .padding(.bottom, 32)
                .onTapGesture {
                    if let toggleLoading {
                        let isSearch = toggleLoading(true, "搜索设备中")
                        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                            toggleLoading(false, "")
                            // TDOO: 这是demo数据，后续要移除
                            devicesListData =  (0..<31).map { DeviceInfo(
                                title: "SM-MI \($0)",
                                macAddress: "D4:F0:EA:C0:93:9B"
                            )}
                        }
                    }
                }
        }
    }
}

//#Preview {
//    SearchDeviceListView()
//}
