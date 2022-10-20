package com.bumble.appyx.samples.common.profile

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.bumble.appyx.samples.common.R
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
@Suppress("MagicNumber")
data class Profile(
    val name: String,
    val age: Int = Random.nextInt(20, 35),
    val city: String = "London",
    val uni: String = "Oxford University",
    @DrawableRes val drawable: Int = drawables.random()
) : Parcelable {

    companion object {
        val eve = Profile(
            name = "Eve",
            drawable = R.drawable.img_8607
        )

        val victoria = Profile(
            name = "Victoria",
            drawable = R.drawable.img_9113
        )

        val brittany = Profile(
            name = "Brittany",
            drawable = R.drawable.brittany8
        )

        val zoe = Profile(
            name = "Zoe",
            drawable = R.drawable.brittany41
        )

        val jill = Profile(
            name = "Jill",
            drawable = R.drawable.brittany60
        )

        val matt = Profile(
            name = "Matt",
            drawable = R.drawable.matt12
        )

        val ryan = Profile(
            name = "Ryan",
            drawable = R.drawable.matt24
        )

        val chris = Profile(
            name = "Chris",
            drawable = R.drawable.matt60
        )

        val daniel = Profile(
            name = "Daniel",
            drawable = R.drawable.matt88
        )

        val imogen = Profile(
            name = "Imogen",
            drawable = R.drawable.imogen15
        )

        val heather = Profile(
            name = "Heather",
            drawable = R.drawable.imogen57
        )

        val maria = Profile(
            name = "Maria",
            drawable = R.drawable.imogen60
        )

        val sophia = Profile(
            name = "Sophia",
            drawable = R.drawable.imogen75
        )

        val allProfiles = listOf(
            eve,
            victoria,
            brittany,
            zoe,
            jill,
            matt,
            ryan,
            chris,
            daniel,
            imogen,
            heather,
            maria,
            sophia,
        )
    }
}

private val drawables: List<Int> = listOf(
    R.drawable.img_8607,
    R.drawable.img_9113,
    R.drawable.brittany8,
    R.drawable.brittany41,
    R.drawable.brittany60,
    R.drawable.matt12,
    R.drawable.matt24,
    R.drawable.matt60,
    R.drawable.matt88,
    R.drawable.imogen15,
    R.drawable.imogen57,
    R.drawable.imogen60,
    R.drawable.imogen75
)
