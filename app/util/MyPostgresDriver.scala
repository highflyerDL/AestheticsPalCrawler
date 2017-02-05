/**
  * Created by sharius on 1/29/17.
  */
package util

import com.github.tminglei.slickpg._

trait MyPostgresDriver extends ExPostgresDriver
  with PgArraySupport
  with PgNetSupport
  with PgLTreeSupport
  with PgRangeSupport
  with PgHStoreSupport
  with PgSearchSupport {

  ///
  override val api = new API with ArrayImplicits
    with NetImplicits
    with LTreeImplicits
    with RangeImplicits
    with HStoreImplicits
    with SearchImplicits
    with SearchAssistants {}
}

object MyPostgresDriver extends MyPostgresDriver
