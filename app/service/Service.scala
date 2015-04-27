package service

import model.Model.User

trait Service {

  def getUser(id: Int): User
}


class ServiceV1 extends Service {

  def getUser(id: Int): User = User("bob1@gmail.com")

}


class ServiceV2 extends Service {

  def getUser(id: Int): User = User("bob2@gmail.com")

}

