import SwiftUI

struct SearchDeviceListView: View {
    let systemHeight:CGFloat = UIScreen.main.bounds.height - 200
    var toggleLoading: ((Bool, String) -> Bool)?

    var body: some View {
        VStack(spacing: 0) {
            List {
                ForEach(1..<30) { item in
                    HStack {
                        VStack(alignment: .leading) {
                            Text("SMI-M1 \(item)")
                                .font(.system(size: 17))
                                .padding(.bottom, 2)
                            Text("D4:F0:EA:C0:93:9B")
                                .font(.system(size: 15))
                                .fontWeight(.thin)
                                .foregroundColor(Color(red: 136/255, green: 136/255, blue: 136/255))
                        }
                        Spacer()
                        Button(action: {
                            print("Button \(item) clicked")
                        }) {
                            Text("链接")
                                .frame(width: 68, height: 32)
                                .font(.system(size: 18))
                                .fontWeight(.thin)
                                .background(Color(red: 232/255, green: 243/255, blue: 1))
                                .foregroundColor(Color(red: 22/255, green: 93/255, blue: 1))
                                .cornerRadius(16)
                                .onTapGesture {
                                    if let toggleLoading {
                                        let isConnecting = toggleLoading(true, "链接中")
                                        if isConnecting {
                                            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                                                toggleLoading(false, "")
                                            }
                                        }
                                    }
                                }
                        }
                    }
                    .frame(maxHeight: .infinity)
                }
            }
            .background(Color.white)
            .listStyle(PlainListStyle())
            .padding(.bottom, 48)

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
                        }
                    }
                }
        
        }
    }
}

#Preview {
    SearchDeviceListView()
}
