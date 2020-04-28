package me.moe.logbot.extensions

import me.aberrantfox.kjdautils.api.dsl.EmbedDSLHandle

fun EmbedDSLHandle.createContinuableField(primaryTitle: String, content: String) = content
        .chunked(1024)
        .mapIndexed { index, chunk ->
            field {
                name = if (index == 0) primaryTitle else "(cont)"
                value = chunk
                inline = false
            }
        }
