@file:Suppress("SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection")

package uz.gita.dima.waziypalar.di

import android.app.AlertDialog
import android.content.Context
import com.appsflyer.AppsFlyerLib
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import uz.gita.dima.waziypalar.utils.AppEventTracking
import uz.gita.dima.waziypalar.presenter.adapter.main_adapter.TodoAdapter
import uz.gita.dima.waziypalar.presenter.adapter.viewpager_adapter.ViewPagerAdapter
import uz.gita.dima.waziypalar.utils.DateTimePicker
import java.util.*

/** [ActivityModule] provides dependencies through activity level injections*/

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun provideAppEventTracking(@ActivityContext context: Context, appsFlyerLib: AppsFlyerLib) =
        AppEventTracking(context, appsFlyerLib)

    @Provides
    fun provideDatetimePicker(calendar: Calendar) =
        DateTimePicker(calendar)

    @Provides
    fun providesTodoAdapter(): TodoAdapter =
        TodoAdapter()

    @Provides
    fun provideViewPagerAdapter(): ViewPagerAdapter =
        ViewPagerAdapter()

    @Provides
    fun providesAlertDialog(@ActivityContext context: Context): AlertDialog.Builder {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setMessage("Do you want to delete the task")
        alertDialog.setCancelable(true)
        return alertDialog
    }
}