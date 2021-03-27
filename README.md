# fragment_not_attached

Application to simulate the fragment not attached crash issue. 

Below is the crash log:
```
2021-03-27 14:04:00.205 22028-22028/com.dialog.fragmentnotattached E/AndroidRuntime: FATAL EXCEPTION: main
    Process: com.dialog.fragmentnotattached, PID: 22028
    java.lang.IllegalStateException: Fragment TestFragment{30eb630 (2449ae4f-b8cc-44c4-9bea-4723747d2f9d)} not attached to a context.
        at androidx.fragment.app.Fragment.requireContext(Fragment.java:774)
        at androidx.fragment.app.Fragment.getResources(Fragment.java:838)
        at androidx.fragment.app.Fragment.getString(Fragment.java:860)
        at com.dialog.fragmentnotattached.TestFragment.onActivityResult(TestFragment.kt:62)
        at com.dialog.fragmentnotattached.ImportPhotoBottomSheetFragment.setResults(ImportPhotoBottomSheetFragment.kt:53)
        at com.dialog.fragmentnotattached.ImportPhotoBottomSheetFragment.access$setResults(ImportPhotoBottomSheetFragment.kt:14)
        at com.dialog.fragmentnotattached.ImportPhotoBottomSheetFragment$simulatePhotoImport$1$1.invokeSuspend(ImportPhotoBottomSheetFragment.kt:47)
        at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
        at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:56)
        at android.os.Handler.handleCallback(Handler.java:883)
        at android.os.Handler.dispatchMessage(Handler.java:100)
        at android.os.Looper.loop(Looper.java:214)
        at android.app.ActivityThread.main(ActivityThread.java:7356)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:492)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:930)
2021-03-27 14:04:00.220 22028-22028/com.dialog.fragmentnotattached I/Process: Sending signal. PID: 22028 SIG: 9

```

## Solution

Create below method inside `BaseFragment`, and call it after any async task. 
```
    fun checkIfFragmentAttached(operation: Context.() -> Unit) {
        if (isAdded && context != null) {
            operation(requireContext())
        }
    }
```
