import com.google.inject.AbstractModule
import com.google.inject.name.Names

import javax.inject._
import play.api.Logger
import play.api.inject.ApplicationLifecycle

import services._

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[JPIService])
      .to(classOf[CacheJPIService])
      .asEagerSingleton
  }
}
