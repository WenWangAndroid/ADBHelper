package cn.xhuww.adb.action

import cn.xhuww.adb.data.FragmentInfo
import cn.xhuww.adb.data.ProjectRunData
import cn.xhuww.adb.receiver.GetActivityFragmentsReceiver
import cn.xhuww.adb.receiver.GetTopActivityReceiver
import cn.xhuww.adb.view.TopActivityMessageDialogWrapper
import com.intellij.openapi.actionSystem.AnActionEvent

class GetTopActivityAndFragmentAction : ADBAction() {
    override fun actionPerformed(e: AnActionEvent, projectRunData: ProjectRunData) {
        val device = projectRunData.device
        val receiver = GetTopActivityReceiver { packageName, activityName ->
            //3. 展示详细信息
            val handleMessage: (fragmentInfos: ArrayList<FragmentInfo>?) -> Unit = {
                TopActivityMessageDialogWrapper(packageName, activityName, it).show()
            }

            try {
                //2. 获取Activity 的详细信息
                val shell = "dumpsys activity $packageName/$activityName"
                device.executeShellCommand(shell, GetActivityFragmentsReceiver(handleMessage))
            } catch (e: Exception) {
                error(e)
            }
        }

        try {
            //1. 获取当前画面的Activity名称
            val shell = "dumpsys window | grep mCurrentFocus"
            projectRunData.device.executeShellCommand(shell, receiver)
        } catch (e: Exception) {
            error(e)
        }
    }
}
