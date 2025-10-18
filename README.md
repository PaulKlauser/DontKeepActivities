# Don't Keep Activities

This repo demonstrates the behavior of Android destroying Activity instances when memory pressure increases, to clarify behavior that is inconsistently documented and often misunderstood in the Android dev community. I also want to contrast the use of the "Don't Keep Activities" developer option with setting process limits as tools to find instances where your app is not properly saving state.

## tl;dr
As of 2014, Android *will* in-fact destroy Activity instances that are stopped but still on the backstack, *without* killing the app's process. This means that "Don't keep activities" *does represent* a valid testing state, **but you still shouldn't use it.**

**"Background process limit" will catch *more* state issues than you will find with "Don't keep activities"**.

## "Don't keep activities" vs "Background process limit"

"Don't keep activities" might be a valid testing tool, but it's still not the best, and there is no benefit to choosing it over "Background process limit".

### Opportunity
Your app will experience process death with nearly every user, it's an extremely common scenario that's often overlooked. It's a matter of "when", not "if". However, not many, or even any, will experience the scenario that "Don't keep activities" represents.

In order for your app's process to be killed, it has to be in the background, and as the user thumbs around other apps, the system will eventually kill your app to prioritize memory for others. Unless the user is very frequently going back to your app, or they've downloaded more RAM, your app's process *will* be killed.

Contrast this to the scenario that "Don't keep activities" represents:

* The app needs to have multiple Activities - This is getting less common with Jetpack Compose and a general push towards "single Activity" apps.
* [The memory pressure needs to be created by the app's own Activity stack](https://cs.android.com/android/platform/superproject/main/+/main:frameworks/base/core/java/android/app/ActivityThread.java;l=8651;drc=61197364367c9e404c7da6900658f1b16c42d0da) - Other apps' memory usage doesn't impact Activity destruction, just your own, so there are less opportunities to create memory pressure to begin with.

### Catching bugs
Further, bugs caught by setting "Background process limit" are a *superset* of the bugs caught by "Don't keep activities". If you find a bug using "Don't keep activities", odds are, you would have found the same bug with "Background process limit". However, by only using "Don't keep activities", you may be missing bugs that *would* have otherwise been caught by testing process death.

This is because of the scoping of the state that causes these types of bugs. Not keeping activities means that we will lose any state held in our Activity, but we will maintain state kept in our Application. 

>**Finding an issue because we lost our Activity state *is* useful, but we also need to find issues caused by losing our Application state. Setting "Background process limit" will find both.**

See the demo below to get more concrete with it.

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
