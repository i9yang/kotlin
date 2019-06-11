package ssg.front.search.api.util

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.beans.BeansException
import org.springframework.stereotype.Component

@Component
class BeanUtil : ApplicationContextAware {
    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    companion object {
        private lateinit var context: ApplicationContext
        fun <T> getBean(beanClass: Class<T>): T = context.getBean(beanClass)
    }
}