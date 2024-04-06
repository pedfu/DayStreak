/**
 * ViewModels are generally obtained by using the native class ViewModelProviders:
 *
 * ```kotlin
 * val viewModel = ViewModelProviders.of(activityOrFragment)[MyViewModel::class.java]
 * ```
 *
 * In this approach, the Android framework instantiates the ViewModel, if it doesn't exist already.
 * This instantiation does not use any non-empty constructor the ViewModel may have. It's possible
 * to instantiate a ViewModel with a non-empty constructor by using a factory:
 *
 * ```kotlin
 * val factory = object: ViewModelProvider.Factory {
 *     override fun <T: ViewModel?> create(modelClass: Class<T>): T {
 *         // Build a ViewModel that matches the provided modelClass.
 *     }
 * }
 *
 * val viewModel = ViewModelProviders.of(activityOrFragment, factory)[MyViewModel::class.java]
 * ```
 *
 * The methods provided in this file allow using the same mechanism to provide ViewModels, but abstract
 * the factory away. Instead, the instantiation of the ViewModel is delegated to a lambda expression:
 *
 * ```kotlin
 * // Call in the context of an activity or fragment.
 * val viewModel = provideViewModel {
 *     MyViewModel(arguments)
 * }
 * ```
 *
 * A ViewModel may also be lazily provided:
 *
 * ```kotlin
 * // Call in the context of an activity or fragment.
 * val viewModel by lazyViewModel {
 *     MyViewModel(arguments)
 * }
 * ```
 */

package com.pedfu.daystreak.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

class ViewModelCustomProvider<VM : ViewModel>(private val vmClass: Class<VM>, creator: () -> VM) {

    private val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return creator.invoke() as T
        }
    }

    operator fun get(owner: ViewModelStoreOwner): VM = ViewModelProvider(owner, factory)[vmClass]
}

/**
 * Provide the existing ViewModel of an owner. If the ViewModel doesn't exist,
 * a new one will be created by calling the provided `creator` lambda.
 *
 * Usage:
 *
 * ```kotlin
 * val viewModel: MyViewModel = provideViewModel {
 *      MyViewModel(viewModelArguments)
 * }
 * ```
 */
inline fun <reified VM : ViewModel> ViewModelStoreOwner.provideViewModel(
    owner: ViewModelStoreOwner = this,
    noinline creator: () -> VM
): VM {
    return ViewModelCustomProvider(VM::class.java, creator)[owner]
}

/**
 * Provides a lazy reference to the existing ViewModel of an owner. If the ViewModel
 * doesn't exist when that reference is first read, a new one will be created by calling the
 * provided `creator` lambda.
 *
 * Usage:
 *
 * ```kotlin
 * val viewModel: MyViewModel by lazyViewModel(::requireActivity) {
 *      MyViewModel(viewModelArguments)
 * }
 * ```
 */
inline fun <reified VM : ViewModel> ViewModelStoreOwner.lazyViewModel(
    noinline ownerGetter: () -> ViewModelStoreOwner = { this },
    noinline creator: () -> VM
): Lazy<VM> {
    return lazy {
        val owner = ownerGetter()
        provideViewModel(owner, creator)
    }
}

/**
 * Provide the existing ViewModel of an owner. If the ViewModel doesn't exist,
 * a new one will be created by calling the provided `creator` lambda. If an exception is thrown
 * during the call to `creator`, it is logged and null is returned.
 *
 * Usage:
 *
 * ```kotlin
 * val viewModel: MyViewModel? = provideViewModelOrNull {
 *      val viewModelArguments = tryGetValues()!!
 *      MyViewModel(viewModelArguments)
 * }
 * ```
 */
inline fun <reified VM : ViewModel> ViewModelStoreOwner.provideViewModelOrNull(
    owner: ViewModelStoreOwner = this,
    noinline creator: () -> VM
): VM? {
    return try {
        ViewModelCustomProvider(VM::class.java, creator)[owner]
    } catch (ex: Throwable) {
        Log.e("ViewModelCustomProvider", "Unable to provide view model", ex)
        null
    }
}

/**
 * Provides a lazy reference to the existing ViewModel of an owner. If the ViewModel doesn't exist
 * when that reference is first read, a new one will be created by calling the provided `creator`
 * lambda. If an exception is thrown during the call to `creator`, it is logged and null is returned.
 *
 * Usage:
 *
 * ```kotlin
 * val viewModel: MyViewModel? by lazyViewModelOrNull(::requireActivity) {
 *      val viewModelArguments = tryGetValues()!!
 *      MyViewModel(viewModelArguments)
 * }
 * ```
 */
inline fun <reified VM : ViewModel> ViewModelStoreOwner.lazyViewModelOrNull(
    noinline ownerGetter: () -> ViewModelStoreOwner = { this },
    noinline creator: () -> VM
): Lazy<VM?> {
    return lazy {
        val owner = ownerGetter()
        provideViewModelOrNull(owner, creator)
    }
}
