# Don't Keep Activities

This repo demonstrates the behavior of Android destroying Activity instances when memory pressure increases, to clarify behavior that is inconsistently documented and often misunderstood in the Android dev community. I also want to contrast the use of the "Don't Keep Activities" developer option with setting process limits as tools to find instances where your app is not properly saving state.

## Timeline
**MMM YYYY** (I don't know, the dawn of Android?) - "Don't Keep Activities" is added as a developer option.

**Sep 2011** - [Dianne Hackborn explains on StackOverflow](https://stackoverflow.com/questions/7536988/android-app-out-of-memory-issues-tried-everything-and-still-at-a-loss/7576275#7576275) that memory pressure will not lead the OS to destroy Activities without killing the app's process, and that "Don't Keep Activities" is a misleading tool because it leads to a state the OS would never actually put your app in.

**Nov 2011** - [An issue is opened to request the Android documentation be corrected](https://issuetracker.google.com/issues/36934944) to reflect the behavior Dianne described in the SO post above.

**Aug 2014** - [Dianne Hackborn commits a change to the Android framework](https://cs.android.com/android/_/android/platform/frameworks/base/+/89ad456ea49cb62615ebdcac83a2515743bbe5fa) to enable Android to destroy Activity instances while leaving the app's process in-tact. Making her SO post obsolete, and no longer a good reference.

**Mar 2019** - [Someone updates the documentation issue](https://issuetracker.google.com/issues/36934944#comment12) noting that [the documentation has been updated](https://developer.android.com/guide/components/activities/activity-lifecycle#asem) to say that an Activity will **not** be destroyed to reclaim memory without also destroying the entire app's process. Even though this is now no longer true based on Dianne's aforementioned commit in 2014. It's also noted that the [Activity documentation here](https://developer.android.com/reference/android/app/Activity.html) still says that an Activity can be asked to finish to free up memory. We now have inconsistent documentation, and out of date SO posts being cited in the community.

**June 2025** - [Abel leaves this comment in the original SO post that Dianne responded in](https://stackoverflow.com/questions/7536988/android-app-out-of-memory-issues-tried-everything-and-still-at-a-loss/79655927#79655927) explaining that everything is hecked, documentation is inconsistent, and Dianne's original answer is no longer valid in 2025.

**Oct 2025** - I see Abel's comment and sample project, validate that he's correct, tweak it, and create this repo as a guide for others that are rightfully confused by this mess.

## Demo

### Activity Destruction
<img width="1006" height="647" alt="image" src="https://github.com/user-attachments/assets/3b6c110c-5bca-4439-a0bf-6246b6157c2c" />

Run the sample app, new up a few Activities, and start allocating memory. You'll see the Activities in the back stack get destroyed once you've increased memory pressure enough.

### "Don't keep activities" vs "Background process limit"

The "Counter Activity" options demonstrate why "Don't keep activities" isn't as effective as a debugging tool on modern Android apps as setting "Background process limit" is.

`CounterAppStateActivity` - Stores the counter state at the **app** level in a singleton.

`CounterViewModelStateActivity` - Stores the counter state in an **Activity-scoped** ViewModel.

#### Don't keep activities
Observe that with **"Don't keep activities"** set, incrementing the counter, navigating forward, and then back, *only* resets the counter in the `CounterViewModelStateActivity` example, and does **not** reset the counter in the `CounterAppStateActivity` example.

#### Background process limit
However, if you turn **off** "Don't keep activities", and instead set **"Background process limit"** to "No background processes", increment the counter, switch to another app (to force the system to kill your app's process), you'll notice the counter is not maintained in either example.
