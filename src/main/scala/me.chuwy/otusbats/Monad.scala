package me.chuwy.otusbats

trait Monad[F[_]] extends Functor[F] { self =>
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  def point[A](a: A): F[A]

  def flatten[A](fa: F[F[A]]): F[A]
}

object Monad {

  def apply[F[_]](implicit ev: Monad[F]): Monad[F] = ev
  
  implicit def monadOption: Monad[Option] = new Monad[Option] {
    override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.flatMap(a => point(f(a)))
    def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa.flatMap(f)
    def point[A](a: A): Option[A] = Option(a)
    def flatten[A](fa: Option[Option[A]]): Option[A] = fa.flatMap(a => a.map(b => b))
  }

  implicit def monadList: Monad[List] = new Monad[List] {
    override def map[A, B](fa: List[A])(f: A => B): List[B] = fa.flatMap(a => point(f(a)))
    def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = fa.flatMap(f)
    def point[A](a: A): List[A] = List(a)
    def flatten[A](fa: List[List[A]]): List[A] = fa.flatMap(a => a.map(b => b))
  }

  type EitherString[A] = Either[String, A]
  implicit def monadEither: Monad[EitherString] = new Monad[EitherString] {
    def map[A, B](fa: EitherString[A])(f: A => B): EitherString[B] = fa.flatMap(a => point(f(a)))
    
    def flatMap[A, B](fa: EitherString[A])(f: A => EitherString[B]): EitherString[B] = fa.flatMap(f)
    
    def point[A](a: A): EitherString[A] = Right(a)
    
    def flatten[A](fa: EitherString[EitherString[A]]): EitherString[A] = fa.flatMap(a => a.map(b => b))
  }

  // def from[F[_]](p: A => F[A], fm: (F[A], A => F[B]) => F[B]):  Monad[F] = new Monad[F] {
  //   def map[A, B](fa: F[A])(f: A => B): F[B] = fm(fa, a => p(f(a)))
    
  //   def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] = fm(fa, f)
    
  //   def point[A](a: A): F[A] = p(a)
    
  //   def flatten[A](fa: F[F[A]]): F[A] = fm(fa, a => a.map(b => b))
    
  // }

  // implicit def monadOption2: Monad[Option] = from[Option](a => Option(a), (a, f) => a.flatMap(f))
  
}
