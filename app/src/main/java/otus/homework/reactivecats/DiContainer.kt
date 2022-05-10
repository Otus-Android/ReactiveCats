package otus.homework.reactivecats

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    val service: CatsService by lazy { retrofit.create(CatsService::class.java) }

    val subscriptions: CompositeDisposable = CompositeDisposable()

    fun localCatFactsGenerator(context: Context) = LocalCatFactsGenerator(context)
}