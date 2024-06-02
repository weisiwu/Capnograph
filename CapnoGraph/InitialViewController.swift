import UIKit

class InitialViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // 设置背景颜色与 Launch Screen 一致
        self.view.backgroundColor = UIColor.white

        // 添加渐变过渡效果
        UIView.animate(withDuration: 1.0, animations: {
            self.view.alpha = 0.0
        }) { _ in
            // 动画完成后，切换到主视图控制器
            self.showMainViewController()
        }
    }

    func showMainViewController() {
        // 获取主视图控制器
        let mainViewController = MainViewController()
        mainViewController.modalTransitionStyle = .crossDissolve
        mainViewController.modalPresentationStyle = .fullScreen
        self.present(mainViewController, animated: true, completion: nil)
    }
}
