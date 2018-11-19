package com.walenkamp.spotdeal

import android.util.Size
import java.lang.Long.signum


class SizeComparer : Comparator<Size> {
    override fun compare(p0: Size?, p1: Size?) =
            signum(p0!!.width.toLong() * p0.height / p1!!.width.toLong() * p1.height)
}