import SwiftUI

struct SplashView: View {
    @State var fadeInOut = false

    var body: some View {
        VStack {
            Image("WLDIcon")
                .resizable()
                .scaledToFit()
                .frame(width: 277, height: 153)
                .padding(.bottom, 1)
                .opacity(fadeInOut ? 1 : 0)
                .onAppear {
                    withAnimation(.easeIn(duration: 1)) {
                        self.fadeInOut = true
                    }
                }
            Text("万联达信科")
                .font(.system(size: 24))
                .fontWeight(.bold)
                .padding(.bottom, 4)
                .opacity(fadeInOut ? 1 : 0)
                .onAppear {
                    withAnimation(.easeIn(duration: 1)) {
                        self.fadeInOut = true
                    }
                }
            Text("WLD Instruments Co., Ltd")
                .font(.system(size: 16))
                .fontWeight(.thin)
                .opacity(fadeInOut ? 1 : 0)
                .onAppear {
                    withAnimation(.easeIn(duration: 1)) {
                        self.fadeInOut = true
                    }
                }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color.white)
        .ignoresSafeArea()
    }
}

#Preview {
    SplashView()
}
