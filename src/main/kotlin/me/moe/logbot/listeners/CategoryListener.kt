package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.moe.logbot.data.Configuration
import me.moe.logbot.services.LoggingService
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateSlowmodeEvent

class CategoryListener(private val configuration: Configuration, private val logger: LoggingService) {

    @Subscribe
    fun onCategoryCreatedEvent(event: CategoryCreateEvent) {
        if (configuration.isTrackingCategories(event.guild.id))
            logger.buildCategoryCreatedEvent(event)
    }

    @Subscribe
    fun onCategoryDeletedEvent(event: CategoryDeleteEvent) {
        if (configuration.isTrackingCategories(event.guild.id))
            logger.buildCategoryDeletedEvent(event)
    }

    @Subscribe
    fun onCategoryUpdatedEvent(event: CategoryUpdateNameEvent) {
        if (configuration.isTrackingCategories(event.guild.id))
            logger.buildCategoryUpdatedEvent(event)
    }
}