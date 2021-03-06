package cn.xhuww.adb.action

import cn.xhuww.adb.ProjectRunData
import cn.xhuww.adb.receiver.GetFragmentsReceiver
import cn.xhuww.adb.receiver.GetTopActivityReceiver
import cn.xhuww.adb.view.TopActivityMessageDialog
import com.intellij.openapi.actionSystem.AnActionEvent

class GetTopActivityAction : ADBAction() {
    override fun actionPerformed(e: AnActionEvent, projectRunData: ProjectRunData) {
        val device = projectRunData.device
        val receiver = GetTopActivityReceiver { packageName, activityName ->
            //3. 展示详细信息
            val handleMessage: (fragmentInfos: String) -> Unit = {
                TopActivityMessageDialog(packageName, activityName, it).show()
            }
            //2. 获取Activity 的详细信息
            val shell = "dumpsys activity $packageName/$activityName"
            device.executeShellCommand(shell, GetFragmentsReceiver(handleMessage))
        }

        //1. 获取当前画面的Activity名称
        val shell = "dumpsys window | grep mCurrentFocus"
        projectRunData.device.executeShellCommand(shell, receiver)
    }
}
