package otus.homework.reactivecats

import android.content.Context
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class LocalCatFactsGenerator(
    private val context: Context
) {

    private fun randomFact(): Fact {
        val localFactsArray = context.resources.getStringArray(R.array.local_cat_facts)
        return Fact(localFactsArray.random())
    }

    /**
     * Реализуйте функцию otus.homework.reactivecats.LocalCatFactsGenerator#generateCatFact так,
     * чтобы она возвращала Fact со случайной строкой  из массива строк R.array.local_cat_facts
     * обернутую в подходящий стрим(Flowable/Single/Observable и т.п)
     */
    fun generateCatFact(): Single<Fact> {
        return Single.fromCallable {
            return@fromCallable randomFact()
        }
    }

    /**
     * Реализуйте функцию otus.homework.reactivecats.LocalCatFactsGenerator#generateCatFactPeriodically так,
     * чтобы она эмитила Fact со случайной строкой из массива строк R.array.local_cat_facts каждые 2000 миллисекунд.
     * Если вновь заэмиченный Fact совпадает с предыдущим - пропускаем элемент.
     */
    fun generateCatFactPeriodically(): Flowable<Fact> {
        return Flowable.interval(2, TimeUnit.SECONDS).flatMap<Fact?> {
            return@flatMap Publisher {
                it.onNext(randomFact())
                it.onComplete()
            }
        }
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }
}