package otus.homework.reactivecats

import android.content.Context
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class LocalCatFactsGenerator(
    private val context: Context
) {

    private val stringArray: Array<String> by lazy { context.resources.getStringArray(R.array.local_cat_facts) }

    /**
     * Реализуйте функцию otus.homework.reactivecats.LocalCatFactsGenerator#generateCatFact так,
     * чтобы она возвращала Fact со случайной строкой  из массива строк R.array.local_cat_facts
     * обернутую в подходящий стрим(Flowable/Single/Observable и т.п)
     */
    fun generateCatFact(): Single<Fact> {
        return Single.fromCallable { getRandomFact() }
    }

    private fun getRandomFact(): Fact {
        return Fact(text = stringArray[Random.nextInt(stringArray.size)])
    }

    /**
     * Реализуйте функцию otus.homework.reactivecats.LocalCatFactsGenerator#generateCatFactPeriodically так,
     * чтобы она эмитила Fact со случайной строкой из массива строк R.array.local_cat_facts каждые 2000 миллисекунд.
     * Если вновь заэмиченный Fact совпадает с предыдущим - пропускаем элемент.
     */
    fun generateCatFactPeriodically(): Flowable<Fact> {
        return Flowable.create<Fact?>( { emitter ->
            if (emitter.isCancelled) {
                return@create
            }
            emitter.onNext(getRandomFact())
            emitter.onComplete()
        }, BackpressureStrategy.BUFFER)
            .delay(2000L, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.computation())
    }
}