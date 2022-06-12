# JPA활용

## JPA란
* 과거에는 SQL을 하나씩 작성하던 것을 JPA가 나타난 후부터는 코드로 작성할 수 있게 해 주었다.
    * 그럼 JPA가 그에 맞는 sql을 실행해 준다.
* 속도, 생산성과 유지보수 등에서 훨씬 빨라졌다.
* 다만 난이도가 어렵다.
    * 실무에서는 JPA를 사용하면 굉장히 어렵다.
        * JPA는 다른 것들에 비해 객체와 테이블을 잘 설계하고 매핑을 해야 한다.
    * JPA의 내부 동작 방식을 모르고 쓰는 경우가 많다.
        * JPA가 어떤 SQL을 만들어 내고, 언제 실행하는지 이해해야 한다.



## 기존 SQL을 사용하는 문제점
개발 언어는 주로 객체지향 언어이다 + DB는 주로 RDB(관계형 DB)를 사용한다
=> 즉, 객체를 관계형 DB에 관리한다.

### SQL중심적인 코딩의 문제점
* 일단 지루함.
* 코드 완성 후 변경사항이 있을 경우 너무 많은 장소에서 변경해야 한다.
    * 예를 들어 name, email만을 작성했는데, phone을 추가하면 이게 필요한 모든 곳에서 추가해줘야 한다.

### 객체지향 vs 관계형DB의 패러다임 불일치
* 객체지향
    * 필드/메소드 등을 잘 캡슐화 하여 사용하려는 목적이다.
* 관계형DB
    * 데이터를 잘 정규화 하여 보관하려는 목적이다.
1. 상속

![](https://i.imgur.com/Xrpo0tf.png)

객체에서 단순히 상속받아서 저장하면 되는 일을, 관계형 DB에서는 너무 복잡하게 처리해야 한다.
이를 java에서 처리하면 쉽게 할 수 있을 것이다.

2. 연관관계

![](https://i.imgur.com/DxedTDh.png)

여기서 보면 알 수 있는 것이 객체 연관관계에서
Member -> Team은 갈 수 있는데 반대로
Team -> Member로는 갈 수 없다.

테이블 연관관계에서는 이게 양방향이 가능하다.

3. 엔티티 신뢰 문제

![](https://i.imgur.com/8ZeGP7f.png)

여기서 만약에 

```
SELECT M.*, T.m*
FROM MEMBER X
JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID
```

라는 SQL을 실행하면 MEMBER와 TEAM에 해당하는 값들이 구해진다.
여기서 만약에

member.getTeam() 을 하면 team의 정보는 얻어올 수 있지만
member.getOrder()을 하면 order의 정보를 얻어올 수 없다.

그렇다고 매번 member를 다 가져올 수도 없다...

-> JPA를 사용하면 객체를 자바 컬렉션에 저장 하듯이 DB에 저장할 수 있도록 해 준다.

종합하자면 객체지향 언어와 관점지향DB간의 불일치의 해결과 생산성 등을 위해 도입된 것이 JPA이다.

---

## JPA의 개념
* Java Persistence API
* 자바 진영 ORM기술의 표준이다.
    * ORM이란 Object-relational mapping(객체 관계 매핑) -> 참고로 여기서 relational이 관계형 DB의 그 R이다.
    * 객체는 객체대로, 관계형DB는 관계형대로 설계하여 ORM프레임워크가 이를 중간에서 매핑시켜 주는 역할을 한다.
        * 각자 알아서 개발을 하면 ORM이 알아서 매핑을 해주겠다는 것.
* 어플리케이션 - JDBC사이에서 동작한다.
    * 패러다임 불일치를 여기서 해결해 준다.

## JPA사용의 이유
- SQL 중심적 개발이 아닌, 객체 중심 개발을 할 수 있다.
- 생산성이 높다.
    - 코드가 이미 만들어져 있어서 이를 불러서 쓰면 되니까 생산성이 당연히 높다.
- 유지보수가 편하다
    - 필드의 변경 등이 있을 때 SQL을 하나하나 변경할 필요가 없다.
- **패러다임의 불일치 해결**
    - 상속, 연관관계, 객체 그래프 탐색, 비교의 해결
- 성능 최적화
    - 1차 캐시와 동일성 보장
        - 같은 트랜잭션 내에서는 같은 엔티티를 반환한다.
            - 이를 통해 약간의 조회 성능 향상
                - 똑같은 SQL이 실행되면 캐시된 메모리를 그대로 반환시킨다.
                    - 2번 같은걸 부르면 SQL을 1번만 실행하기.
    - 트랜잭션을 지원하는 쓰기 지연 - INSERT
        - 트랜잭션의 begin ~ commit사이에서 사용한 INSERT를 하나하나 진행하지 않고, 한꺼번에 뭉탱이로 처리한다.
            - commit하는 순간 한꺼번에 네트워크를 통해 INSERT처리하기
                - 이는 본래 JDBC batch를 사용하는 기능인데, 이게 코드가 엄청 지저분해 진다.
                    - 이 지저분한 것을 JPA에서 한번에 가능하게 해 준다.
    - 지연로딩 / 즉시로딩(eager, lazy)
        - Lazy방식
            - 객체가 실제 사용될 때 로딩한다.
        - Eager방식
            - Join을 사용하여 한번에 연관된 객체까지 미리 조회한다.
- 데이터 접근 추상화와 벤더 독립성
- 표준

---

## 프로젝트 시작하기

h2 데이터베이스를 다운로드받는다.
버전은 14.1.119로 함.

---

maven으로 프로젝트생성
![](https://i.imgur.com/nLqV53S.png)

![](https://i.imgur.com/4MaqX6f.png)

---

pom.xml 작성

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>jpa-basic</groupId>
    <artifactId>ex1-hello-jpa</artifactId>
    <version>1.0.0</version>
    <dependencies>
        <!-- JPA 하이버네이트 -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>5.6.5.Final</version>
        </dependency>
        <!-- H2 데이터베이스 -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.1.210</version>
        </dependency>
    </dependencies>
</project>
```

-> 하이버네이트나 H2의 경우 자신 상태에 맞춰서 진행해주면 된다.

---

persistence.xml 작성하기

![](https://i.imgur.com/fcVrQBp.png)

해당 위치에 persistence.xml 생성

```
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2"
             xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    <persistence-unit name="hello">
        <properties>
            <!-- 필수 속성 -->
            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="javax.persistence.jdbc.user" value="sa"/>
            <property name="javax.persistence.jdbc.password" value=""/>
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>

            <!-- 옵션 -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.use_sql_comments" value="true"/>
            <!--<property name="hibernate.hbm2ddl.auto" value="create" />-->
        </properties>
    </persistence-unit>
</persistence>
```


---

코드 작성하기(연결 확인하기)

```
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args){
        // persistence unit name을 넘기라 하는데, 이게 hello로 되어 있다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code
        em.close();
        emf.close();
    }
}
```

이렇게 하고 해당 내용을 실행해 보면, 실행이 되고 종료되는 것을 확인 가능하다.

---

### 기초 설정

#### h2실제 사용

localhost:8082에 접속한다.

![](https://i.imgur.com/MvYwD0E.png)

h2에서 다음 코드를 통해 테이블 생성

```
create table Member ( 
 id bigint not null, 
 name varchar(255), 
 primary key (id) 
);
```

#### java객체 생성
![](https://i.imgur.com/4yVjQZF.png)

```
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Member {

    @Id
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
```

---

### Data Insert 진행하기

```
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args){
        // persistence unit name을 넘기라 하는데, 이게 hello로 되어 있다.
        // EntityManagerFactory는 딱 한번 실행해 준다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        // Transaction 단위마다 em을 하나씩 만들어 줘서 진행한다.
        EntityManager em = emf.createEntityManager();

        // 하나의 Transaction 시작
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        //code
        Member member = new Member();
        // member에 값을 저장한다.
        member.setId(1L);
        member.setName("HelloA");
        // em에 값 설정
        em.persist(member);
        // commit 진행
        tx.commit();

        em.close();
        emf.close();
    }
}
```
JpaMain에서 해당 코드를 실행하면

![](https://i.imgur.com/JuMLMni.png)

IntelliJ에서 실행된 코드가 나타나고

![](https://i.imgur.com/ezEkKT9.png)

DB에도 저장된 것을 확인할 수 있다.


사실 근데 보면, Transaction이 실행되면 그 사이는 Try ~ catch로 로직을 진행해야 한다. 정석적인 코드는
```
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args){
        // persistence unit name을 넘기라 하는데, 이게 hello로 되어 있다.
        // EntityManagerFactory는 딱 한번 실행해 준다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        // Transaction 단위마다 em을 하나씩 만들어 줘서 진행한다.
        EntityManager em = emf.createEntityManager();

        // 하나의 Transaction 시작
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            //code
            Member member = new Member();
            // member에 값을 저장한다.
            member.setId(2L);
            member.setName("HelloB");
            // em에 값 설정
            em.persist(member);
            // commit 진행
            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이렇게 되기는 한데...이거 Spring에서 알아서 해줘서 상관 x

---

### Data find 진행하기

```
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member findMember = em.find(Member.class, 1L);
            System.out.println("id : " + findMember.getId() + ", name = " + findMember.getName());
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

![](https://i.imgur.com/madNDMp.png)

값을 찾아 온 것을 확인할 수 있다.
Long 1의 id를 값는 하나의 Table 데이터를 객체로 가져와서 볼 수 있다.

---

### Data Update 진행하기

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloChan!");

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

![](https://i.imgur.com/0s804ef.png)

![](https://i.imgur.com/mlBpojr.png)

코드를 보면 그냥 findMEmber.setName에서 이름을 저장해 준 것 만으로도 H2내의 객체를 Update해준다.
-> 자바 객체에서 값을 바꿨을 뿐인데 DB에서도 바꿨다.

그 이유는 JPA에서 자바 객체를 관리하며 이 값이 바뀌었는지 아닌지에 대해 Transaction이 끝나는 시점에서 확인을 한다.
그래서 그 값이 바뀌게 되면 그걸 캐치하여 commit해 주는 것이다.
만약 동일한 값으로 set해주고 진행하면 update가 되지 않는 것을 확인할 수 있을 것이다.

JPA의 모든 데이터 변경은 트랜잭션 안에서 실행한다.

---

### JPQL
간단히 말하자면, 내가 원하는 쿼리를 만들어 내기 위한 방법이다.

* createQuery
쿼리와 비슷하게 JPQL을 사용하여 호출할 수 있다.
이를 사용하면 쿼리와 같은 방식으로 객체를 다룰 수 있도록 해 주며 추가적인 기능을 부여할 수 있다. (**객체지향 쿼리**)
예를 들어 페이징을 하는 경우

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
//            Member findMember = em.find(Member.class, 1L);
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(5)
                    .setMaxResults(8)
                    .getResultList();

            for(Member member : result) System.out.println("name = " + member.getName());

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이런 식으로 하면 
![](https://i.imgur.com/nXDzG0s.png)
이런 식으로 알아서 페이징까지 진행해 준다.

정리하자면
> JPA를 사용하면 엔티티 객체를 중심으로 개발을 하게 되는데, 검색을 할 때에도 엔티티 객체를 대상으로 검색한다.

> 여러 검색 조건이 필요한 SQL이 필요할 때에는 JPQL을 사용하게 된다.

---

## 영속성 컨텍스트
* JPA에서 가장 중요한 2가지
    * 객체와 관계형DB의 매핑
    * 영속성 컨텍스트
        * 실제 JPA가 내부에서 어떻게 동작하는지에 관해

### 엔티티매니저 팩토리, 엔티티매니저
![](https://i.imgur.com/DJYYtiq.png)

1. 고객의 요청이 있을 때마다 Factory가 manager를 하나씩 만들어 준다.
2. 이 manager가 DB conn을 통해 DB를 사용한다.

### 영속성 컨텍스트란?
* JPA를 이애하는데 가장 중요한 용어이다.
* **엔티티를 영구 저장하는 환경** 이라는 뜻
* EntityManager.persist(entity)
    * persist가 실제로는 DB에 저장하는 것이 아니라, 영속화 한다는 것이다.


이 EntityManager은 실제로 눈에 보이는 것은 아니고, 논리적인 개념이다.
엔티티 매니저를 통해 영속성 컨텍스트에 접근한다.

> 엔티티 매니저와 영속성 컨텍스트가 1:1로 매핑되어 있다.
![](https://i.imgur.com/Z40jmZ2.png)

### 엔티티의 생명주기
* 비영속 (new/transient)
    * 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
* 영속(managed)
    * 영속성 컨텍스트에 관리되는 상태
* 준영속 (detached)
    * 영속성 컨텍스트에 저장되었다가 분리된 상태 
* 삭제 (removed)
    * 삭제된 상태

#### 비영속
![](https://i.imgur.com/rLIHa0u.png)
고냥 세팅만 해둔 상태이다.(걍 객체만 생성한거다)

#### 영속
![](https://i.imgur.com/mQsSPl2.png)
객체 생성 후, entitymanager에 persist를 사용해 집어넣고 나면 영속 상태가 된다.
이는 entitymanager 안에 member가 들어갔다는 것이다.

## 영속성 컨텍스트의 이점
* 1차 캐시
* 동일성 보장
* 트랜잭션을 지원하는 쓰기 지연(tansactional write-behind)
* 변경 감지(Dirty Checking)
* 지연 로딩(Lazy Loading)

### 엔티티 조회, 1차 캐시
미리 검색했거나, 저장해 둔 값을 캐싱해 두고 이후 같은 값을 검색하면 이걸 가져와서 사용한다.
약간의 이득을 주지만, 실질 큰 도움은 되지 않는다. (1차 캐시는 하나의 Transaction내에서만 이루어 지기 때문이다.)

코드를 통해 확인해 보자면
```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            // 비영속
            Member member = new Member();
            member.setId(100L);
            member.setName("HelloJPA");

            // 영속
            em.persist(member);

            Member findMember = em.find(Member.class, 100L);
            System.out.println(findMember.getName());

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

![](https://i.imgur.com/KJYaZno.png)

이와 같이, 저 100L의 값은 아직 DataBase에 insert되지 않은 상태인데, 저 tx의 내에서 동작하게 되면 바로 값을 가져오게 된다.
즉, DB를 통하지 않고, 이미 객체 값이 있다면 바로 캐시를 통해 이를 가져오는 것이다.

혹은 select만을 진행할 때에도

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            // 비영속
//            Member member = new Member();
//            member.setId(100L);
//            member.setName("HelloJPA");

            // 영속
//            em.persist(member);

            Member findMember1 = em.find(Member.class, 100L);
            Member findMember2 = em.find(Member.class, 100L);
            Member findMember3 = em.find(Member.class, 100L);
            System.out.println(findMember1.getName());
            System.out.println(findMember2.getName());
            System.out.println(findMember3.getName());

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

![](https://i.imgur.com/uTgk91m.png)

이렇게 하나의 select를 해서 모든 값을 캐싱하여 가져오게 해 준다.

---

### 동일성 보장

JPA는 java의 collection에서 동일 레퍼런스를 통해 데이터를 가져왔을 때에 주소가 같은 것 처럼, 영속 entity의 동일성을 보장시켜 준다.

-> 위의 1차 캐시에서 가져온 개념으로 생각 가능.

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            // 비영속
//            Member member = new Member();
//            member.setId(100L);
//            member.setName("HelloJPA");

            // 영속
//            em.persist(member);

            Member findMember1 = em.find(Member.class, 100L);
            Member findMember2 = em.find(Member.class, 100L);

            System.out.println(findMember1 == findMember2);

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

![](https://i.imgur.com/QjuHyjs.png)

---

### 트랜잭션을 지원하는 쓰기 지연
여러 개의 insert가 존재하면, 이를 가지고 있다가 commit하는 순간에 insert 해 준다.

![](https://i.imgur.com/BzZTF4E.png)

먼저 Member에서 생성자를 만들어 준다.
```
public class Member {

    @Id
    private Long id;
    private String name;

    // 생성자를 하나 만들어 준다.
    public Member(){}
    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member1 = new Member(120L, "A");
            Member member2 = new Member(121L, "B");
            Member member3 = new Member(122L, "C");

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            System.out.println("----------------");

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

![](https://i.imgur.com/CEa5zWy.png)

보면 구분선(-------) 을 통해 구분하였는데, insert는 구분선보다 아래에서 이루어졌다.
이는 지연되어 insert되었다는 것이다.(HDBC Batch)

---

### 변경 감지(수정)

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = em.find(Member.class, 120L);
            member.setName("chan~");
            // em.persist(member) 을 해서는 안됨.
            
            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```


![](https://i.imgur.com/7C6YOMD.png)

이게 되는 이유는
1. JPA는 transaction이 commit되는 시점이 flush()를 실행시킨다
2. 이후 Entity와 스냅샷을 비교한다. (스냅샷이란, 처음 DB에서 값이 들어왔을 때의 상태를 찍어두는 것이라 생각하면 된다.)
3. Transaction시점에서 둘을 비교하여, 값이 바뀌었을 경우 UPDATE쿼리를 쓰기 지연 SQL저장소에 저장시켜 두고, 이후 UPDATE 쿼리가 반영되는 것이다.

![](https://i.imgur.com/Saq5v9x.png)

#### Flush
저기서 사용되는 Flush가 뭘까?

Flush는 이전에 쌓아둔 (Insert, Update, Delete등) 을 DB에 날리는 것
이는 영속성 컨텍스트의 변경 사항과 DB를 맞추어 주는 작업이다.

* 플러시 발생
    * Transaction시행시 자동 수행
    * 변경 감지
    * 수정된 엔티티 쓰기 지연 SQL저장소에 등록
    * 쓰기 지연 SQL저장소의 쿼리를 데이터베이스에 전송함
* 영속성 컨텍스트 플러시 방법
    * em.flush
        * 직접 호출
    * Transaction commit시 자동 호출
    * JPQL쿼리 실행 시 자동 호출
        * 그 이유는 JPQL의 경우 여러 데이터를 호출하는 등의 역할을 하기 때문에 DB에 우리가 만들고 있는 값들도 있어야 한다. 그렇기 때문에 미리 앞의 값들을 저장해두고 실행하게 된다.

> 정리
1. 플러시는 영속성 컨텍스트를 비우지 않는다.
2. 영속성 컨텍스트의 변경 내용을 DB에 동기화한다.
3. commit직전에 동기화시켜준다.


---

### 엔티티 삭제
삭제함ㅇㅇ

---

### 준영속 상태
영속 -> 준영속
영속 상태의 엔티티가 영속성 컨텍스트에서 분리된다.(detached)
영속성 컨텍스트가 제공하는 기능을 사용하지 못한다.

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = em.find(Member.class, 120L);
            member.setName("chan222~");

            // 준영속화 해준다.
            em.detach(member);

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

![](https://i.imgur.com/SC06PCi.png)

보면 원래 DB에서 member를 가져왔을 때, 이는 영속화가 되어 있다.
근데 이를 detach하여 준영속화 해주면, 더이상 JPA에서 해당 객체를 영속화하지 않는다.
따라서, 내용을 바꾸어도 실제로는 영속화되지 않기 때문에 flush의 대상이 되지 않을 것이다.

참고로 em.clear() 를 사용하면 해당 em의 값 전체를 싹다 준영속화해버린다.

영속성 초기화를 해주면 당연히 1차 캐시 이런것의 대상도 되지 않는다. 만약 select를 동일한 객체에 진행하면 쿼리를 다시 호출할 것이다.

* 준영속 상태 만드는 방법
    * em.detach()
        * 해당 하나를 준영속화
    * em.clear()
        * 해당 em 전체를 준영속화

---


--> DDL자동생성 사용
![](https://i.imgur.com/PDYwFV9.png)
1. create
기존 테이블 삭제 후 다시 생성
즉, 기존에 있던 해당 이름을 가진 테이블을 모두 삭제하고, 다시 Entity로 Table을 생성해 준다.
2. create-drop
어플리케이션이 종료되는 시점에 Table이 을 drop한다.
보통 Test Case사용할 때 끝나고 걍 다 지워버리려고 사용한다.
3. update
변경되는 경우, 이를 적용해 준다.(참고로 추가나 변경은 가능한데, 지우는건 안된다.)
4. validate
정상 매핑이 되었는지 확인하는 용도로만 쓴다.
5. none
auto기능을 쓰기 싫으면 걍 이거 쓴다.
암거나 써도 되는데 관례상 이거 씀.

## 엔티티 매핑
### 객체와 테이블 매핑
@Entity가 붙은 클래스는 JPA가 관리하고, 엔티티라 한다.
JPA를 사용해서 테이블과 매핑할 클래스는 @Entity가 필수
* 주의
    * 기본 생성자 필수(파라미터가 없는 public 또는 protected 생성자)
    * final 클래스, enum, interface, inner클래스 사용X
    * 저장할 필드에 final 사용X

#### @Table
> 엔티티와 매핑할 테이블 지정

![](https://i.imgur.com/522NUOt.png)


---

Member클래스를 이렇게 변경하고

```
import javax.persistence.*;
import java.util.Date;
public class Member {

    @Id
    private Long id;

    @Column(name = "name")
    private String username;

    private Integer age;

    // JAVA에서 ENUM으로 만들었을 때, DB에는 보통 enum타입이 없을 것이다.
    // 그 때에 Enumerated타입을 통해 만들어낼 수 있다.
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // 날짜 관련은 Temporal을 사용하고, 3가지 타입이 있다.
    // DATE : 날짜, TIME : 시간, TIMESTAMP : 두개 다 포함
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    // 굉장히 큰 크기를 가진 것을 사용하기 위해서 사용
    @Lob
    private String description;
}
```

동일 패키지에 RoleType 생성
```
public enum RoleType {
    USER, ADMIN
}
```

그리고 기존 JpaMain코드에서 문제가 될 부분을 없애 주고 

persistence에서 해당 내용 주석 해제
```
<property name="hibernate.hbm2ddl.auto" value="create" />
```

이후 실행해 주면

![](https://i.imgur.com/VCioPyS.png)

이렇게 알아서 만들어 준다.

사용 가능한 기능은
![](https://i.imgur.com/f7vn6bF.png)
다음과 같다.

1. @Column
![](https://i.imgur.com/NvC4AvC.png)

* name
    * 이름
* insertable / updatable
    * 등록 / 변경 가능 여부
        * 만약에 updatable = false 이러면 여기서의 변경이 반영되지 않는다.
* nullable
    * null허용 여부를 넣는다.
        * false -> not null
* unique
    * unique 제약조건을 넣어준다.
        * 근데 이거는 잘 안쓰고 다른 방식을 사용한다. Table에서 선언하곤 함.
* columnDefinition
    * 컬럼 정보를 직접 선언해줄 수 있다.
* length
    * 길이 제약조건을 주고, String에서만 사용 가능
* precision, scale
    * BigDecimal에서 아주 큰 숫자 등에서 사용 가능

2. @Enumerated
Enum타입에서 값들을 넣어 준다.
근데 이게 두가지 방법이 있는데, 기본은 ORDINAL이다.
* EnumType.ORDINAL
    * 값을 순서대로 0, 1, 2, ....이렇게 세어서 순서로 저장함.
        * 사용하지 말자!
            * 중간에 새로운 값이 enum에 추가되면 이거 큰일남.
* EnumType.String
    * 갑의 이름을 고대로 저장한다.
        * 이걸로 쓰자.

3. @Temporal
날짜 타입을 매핑할 때 사용.
근데 이거 요즘 잘 안쓰는데.. 그 이유는 java8이후로 현재 지원해주는
LocalDate(연월) / LocalDateTime(연월일) 요렇게 쓰면 알아서 해준다.

4. Lob
DB의 BLOB, CLOB과 매핑된다.
따로 설정을 해줄수는 없고, 필드 타입에 맞춰서 해준다.
* BLOB
    * byte[], java.sql.BLOB
* CLOB
    * String, char[], java.sql.CLOB

5. Transient
매핑 하기 싫을때 사용.
걍 메모리에서 임시로 값을 보관할 때에 사용.

---

## 기본키 매핑

### 매핑 방법
* 직접 할당
    * @ID
* 자동 할당(@GeneratedValue)
    * IDENTITY
        * DB에 위임, MYSQL
    * SEQUENCE
        * DB시퀀스 오브젝트 사용, ORACLE
            * @SequenceGenerator 필요
    * TABLE
        * 키 생성용 테이블 사용, 모든 DB
            * @TableGenerator 필요
    * AUTO
        * 방언에 따라 자동 지정, 기본값

#### INDENTITY

* 기본 키 생성을 DB에 위임.
* 주로 MySql, PostgreSQL, SQL Server, DB2에서 사용한다.
* JPA는 보통 트랜잭션 커밋 시점에 INSERT SQL실행
* AUTO_INCREMENT는 DB에 INSERT SQL을 실행한 이후에 ID값을 알 수 있다.
* **INDENTITY전략은 em.persist()시점에 즉시 INSERT SQL을 실행하고 DB에서 식별자를 조회한다.**

이 전략은 DB에서 변경사항을 위임하는 것이다.
근데 이게 문제가 뭐냐면 내가 ID에 값을 넣을 수가 아예 없고, DB에서만 해당 요청이 실행된다.
즉, DB에 값이 들어가 봐야 ID값을 알 수 있다.
이전에 했던 영속성 컨텍스트에서 관리가 되려면 PK가 있어야 한다.

영속성컨텍스트에서 관리하려면 PK가 있어야 함.
근데 DB에 값이 들어가야 PK가 생김.

이 문제를 해결하기 위해, em.persist()호출 시점에 바로 INSERT쿼리를 날린다.
이렇게 INSERT가 완료되고 나면 그제서야 ID를 알 수 있다.

추가로 insert를 하고 ID를 안다는 것이, select를 추가로 진행하는 것은 아니다.
JDBC에서 알아서 insert할 때 값을 받아오기 때문에 이미 값은 저장되어 있는 것이다.

여기서는 insert를 한꺼번에 보내는 지연 전략이 의미가 없어지게 된다.

#### SEQUENCE
* DB시퀀스는 유일한 값을 순서대로 생성하는 특별한 DB오브젝트(ORACLE의 SQUENCE같은거)
* 오라클, PostgreSQL, DB2, H2 등에서 사용한다.

![](https://i.imgur.com/5Dky5cR.png)

![](https://i.imgur.com/jqKIeCh.png)

참고로 이거는 insert한꺼번에 보내는것이 가능하다.
그게 가능한 이유는 필요할 때에 sequence를 호출하여 값을 받아올 수 있기 때문임.

> **성능 최적화**

이게 근데 문제는 매번 값을 받기 위해 네트워크를 왔다갔다 해야하는데...오히려 성능에 문제가 생기는게 아닌가? 할 수 있다.
이를 해결할 때 사용하는 것이 allocationSize / initialValue이다.

처음 next sequence를 call할 때에 시작점을 allocationSize에 설정한 기본값만큼 키워서 진행한다.
만약 id가 2, 3, 4, 5를 갖는 값을 insert하며 id를 확인하려 한다면
1. 처음에 1을 호출한다.
2. id들은 이 1보다 큰 값들을 가진다. 다음은 50을 호출한다.
3. 이렇게 하면 sequence는 값이 늘어나 있고, id들은 메모리 상에서 빈 값에서 나타나기 때문에 더이상 호출을 할 필요 없이 값이 저장될 수 있다.
4. 여러번 왔다갔다 할 필요 없으므로 성능이 개선된다.
5. 50개보다 많은 값들을 넣으려 하면 한번 더 call해 올 것이다.

다만 이게 막 크기를 10000 이렇게 하면 더 좋긴 하지만 sequence에 구멍이 뻥뻥 뚫리게 되므로 보통 50, 100정도로 사용한다고 한다.
참고로 이거 동시성 이슈도 생기지 않는다. 자신이 호출한 값을 그냥 받아오기 때문이다.

#### TABLE
* 키 전용 테이블을 하나 만들어서 DB SEQUENCE를 흉내내는 것이다.
* 장점은 모든 DB에서 사용 가능
* 단점은 성능이 떨어짐.

![](https://i.imgur.com/jYO847M.png)

![](https://i.imgur.com/nnvCU2E.png)

이거 쓸바에는 위에 두개중에 하나 선택해 쓰는것이 좋다.
그냥 내 생각에는 SEQUENCE가 가장 좋을듯 싶다.

### 식별자 전략
* 기본 키 제약 조건
    * Null비허용, 유일성, 변하면 안된다.

> 저 변하면 안되는 것을 만족하는 방법은 사용하기 힘들다.
> 왜냐면 10, 20, 100년 뒤까지 이게 쭉쭉 사용되면 변할 수 있기 때문이다.
> 따라서 자연키(주민번호 이런거... 변경될 여지가 있기도 하고 저장하면 안되기도 하고.) 대신 대리키(대체키)를 사용하도록 한다.

권장하는 방법은 -> Long형 + 대체키(sequence 등) + 키 생성전략 사용
AUTO_INCREMENT나 SEQUENCE이런거 걍 쓰면 될거같다.

---

## 연관관계 매핑

### 객체 모델링 진행시

* Member Entity 코드
```
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

    @Column(name = "TEAM_ID")
    private Long teamId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
```

---

* Team Entity코드
```
@Entity
public class Team {
    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

---

* JpaMain코드
```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Team team = new Team();
            team.setName("TeamA");

            em.persist(team);

            Member member = new Member();
            member.setName("member1");
            member.setTeamId(team.getId());
            em.persist(member);
            
            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

---

이렇게 하고 실행하면
![](https://i.imgur.com/WMXXcDZ.png)
이렇게 member에서 TEAM_ID에 해당하는 id가 저장되게 될 것이다.

이런 식으로 하면, 이후에 관련해서 변경하거나 조회하는 경우 가져온 식별자를 통해 다시 검색하고... 이렇게 하게 된다.
이는 객체 지향적인 방법이 아니다.

즉 **객체를 테이블에 맞추어 데이터 중심으로 모델링하면, 협력 관계를 만들 수 없다.**
* 테이블은 외래키로 조인을 사용하여 연관된 테이블을 찾는다.
* 객체는 참조를 사용하여 연관된 객체를 찾는다.

둘의 패러다임이 완전히 다르다.

---

### 단방향 연관관계

먼저 Member가 Team을 참조하게 만들어 보도록 하자.

* Member Entity 수정
```
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    // 1대N 매핑. 이는 Team을 참조한다.
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
```

Member의 코드를 다음과 같이 바꾼다.
단방향 매핑이기 때문에 Member에만 team을 참조로 넣어준다.

---

* JpaMain 수정

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Team team = new Team();
            team.setName("TeamA");

            em.persist(team);

            Member member = new Member();
            member.setName("member1");
            member.setTeam(team);
            em.persist(member);

            Member findMember = em.find(Member.class, member.getId());

            Team findTeam = findMember.getTeam();
            System.out.println("findTeam = " + findTeam.getName());

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

JpaMain에서 Member를 통해 Team을 바로 세팅해서 넣어주고, 값을 꺼내온다.

---

![](https://i.imgur.com/MsaW1UE.png)

이렇게 TeamA의 값을 참조하여 바로 가져올 수 있다.
insert보다 먼저 가져온 이유는 당연히 insert가 commit될 때 지연되어 이루어지고 team의 값들은 영속화되어 캐시되었기 때문.

---

### 양방향 연관관계

* member코드

```
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    // 1대N 매핑. 이는 Team을 참조한다.
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
```

---

* Team 코드

```
@Entity
public class Team {
    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")       // mappedBy는 일대다 매핑에서 반대쪽에는 어떤 것과 연결되어 있는지를 보여준다.
    private List<Member> members = new ArrayList<Member>();

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

양방향 매핑을 위해 Team에서도 members를 참조하는 ArrayList를 만들어 준다.
해당 ArrayList는 member에서 Team을 참조하는 값을 mappedBy로 나타내 준다.

---

* JpaMain 코드

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            List<Member> members = findMember.getTeam().getMembers();

            for(Member m : members){
                System.out.println("m = " + m.getUsername());
            }

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

양방향 매핑을 확인하기 위해 Insert 후 Team에서 member을 찾아 준다.
참고로 em.flush를 통한 영속화를 미리 진행해 주어 먼저 insert를 진행할 수 있도록 한다.

![](https://i.imgur.com/0uTPBE0.png)
![](https://i.imgur.com/Sgnh9zP.png)

insert를 하고, 제대로 값이 들어간 것을 확인할 수 있다.

---

#### mappedBy
저기서 mappedBy의 용도가 매우 중요하다.
객체와 연관관계 간의 차이를 이해해야 한다.

객체에서 양방향 연관관계는 사실 단방향 연관관계가 2개 있는 것이다.
근데 Table에서의 연관관계는 양방향 1개로 이루어져 있다.(FK 하나로 다 가능하다.)

여기서 이제 문제가 생긴다.
예를 들어 Member와 Team이 양방향 매핑된다고 하자(다대일)

Member A는 Team A에 소속되어 있고
이 Member A가 Team B로 소속을 옮긴다 하면
이는 DB에서는 Member의 FK를 바꾸어 주면 될것이다.
근데 객체에서 진행하려 하면 어디서 바꿔야 할지??
단방향이면 하나만 바꾸면 되는데 양방향이면 둘 다 바꿔야 하기 때문..

이를 해결하기 위해 **둘 중 하나로 외래키를 관리하도록 정해야 한다.**
즉, DB의 FK에 해당하는 부분을 Member객체에서 관리할지, Team객체에서 관리할지에 관한 것이 바로 연관관계의 주인이다.

* 연관관계의 주인
    * 양방향 매핑 규칙
        * 객체의 두 관계중 하나를 연관관계의 주인으로 지정
        * 연관관계의 주인만이 외래 키를 관리(등록, 수정)
        * 주인이 아닌 쪽은 읽기만 가능하다.
        * 주인은 mappedBy속성을 사용하면 안된다.
            * 이 mappedBy의 뜻은 이것에 의해 mapping되었다는 것이기 때문에 당연히 그 mappedBy로 되어있는쪽이 주인이다.
        * 주인이 아니면 mappedBy속성으로 주인을 지정하면 된다.
            * mappedBy가 적용되면 그것으로는 수정이 불가능하고 조회만 가능하다.

그럼 누구를 주인으로 정해야 할까?
* 외래키가 있는쪽을 주인으로 하는게 좋다.
* 즉 Member.team이 연관관계의 주인이 된다.

#### 양방향 매핑 중 가장 많이 하는 실수
연관관계의 주인에 값을 입력하지 않음.

JpaMain에서

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            Team team = new Team();
            team.setName("TeamA");
            team.getMembers().add(member);
            em.persist(team);

            em.flush();
            em.clear();

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이런 코드를 입력하고 실행하면

![](https://i.imgur.com/2as6Vcf.png)

insert가 실행된다. 그런데 실제로 h2에서 확인해 보면

![](https://i.imgur.com/JwJ9Sfc.png)

member에서 TEAM_ID가 null인 것을 확인할 수 있다.
이는 아까 적혀있던대로 team의 member객체는 mappedBy가 사용되어 연관관계의 주인이 아니라서 값을 넣을 수 없기 때문이다. 그렇기 때문에 값을 넣으려면 FK가 있는 연관관계의 주인, 즉 Member에서 값 적용을 해 주어야 한다.

---

* JpaMain코드를 수정해 준다.
```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이렇게 하고 실행하면 연관관계의 주인인 member에서 값을 넣어주었기 때문에 제대로 들어갔을 것이다.

![](https://i.imgur.com/0bijZ6g.png)

이렇게 잘 들어간 것을 확인할 수 있다.

---

그런데, 실제로 할 때에는 그냥 연관관계의 주인이나 이런거 상관없이 둘 다 세팅해주면 된다.
왜냐면 JPA에서 1차 캐싱을 지원하기 때문에 만약 team에 member가 세팅되어 있지 않은 상태에서 값을 가져오면 캐싱되어 있는 값을 가져오기 때문이다.
그리고 Test Case를 작성할 때에는 java collection으로 동작하기 때문에, 이 때를 위해서도 양쪽 모두 세팅해 주면 된다.

다만 어디에 세팅해야 값이 들어가는지는 알고 있어야 할듯.

### **팁**
위에서 세팅을 해 줄 때 굳이 양방향을 하나하나 세팅해 줄 필요가 없다..
* Member에서 setTeam을 할 때에
```
public void setTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);        // 주인쪽에서 set해주면 바로 다른쪽에도 세팅을 진행해 준다.
    }
```
이렇게 해 주면, 바로 setTeam을 통해 매핑해 줄 때 연관관계 주인의 반대쪽에도 set이 걸리기 때문이다.

혹시 반대쪽을 기준으로 넣고 싶다면
* Team에 member를 세팅해줄 addMember메소드만 하나 추가하여
```
    public void addMember(Member member){
        member.setTeam(this);
        members.add(member);
    }
```
이렇게 하면 반대로도 가능하다.
상황마다 맞춰서 해주기.

참고로 이거 양쪽 다 해주면 안된다...하나만 해주기.

#### 양방향 매핑시 무한 루프 가능성
toString, lombok, JSON생성 등에서

만약 Member에서 Team을 불러오는 경우
1. Member의 참조객체 Team을 불러온다.
2. Team의 참조객체 Member를 불러온다.
3. Member의 참조객체 Team을 불러온다.
4. Team의 참조객체 Member를 불러온다.
5. 무한반복.....

이런 일이 벌어진다.

사실 근데 controller에서 Entity를 반환하지 않으면 된다. 
DTO쓰면 참조를 무한으로 안할듯??

---

### 정리
단방향 매핑만으로도 이미 연관관계 매핑은 실질 완료된 상태이다.
-> 처음 설계할 때에는 양방향으로 하지 말고 단방향으로 완료해야 한다.
왜냐면 양방향 매핑이라는 것은 사실 반대쪽에서 조회하는 방법을 추가하는 것이다.

단방향 매핑을 하고 나면 실질 테이블 정의는 완료된 것이다.
그러니 그냥 조회가 필요할 때에 Entity쪽을 조금 수정해 주면 된다.

그리고 비즈니스 로직이 아닌, 외래 키의 위치를 기준으로 정해주어야 한다.

---

## 연관관계 매핑
* 다중성
* 단방향, 양방향
* 연관관계의 주인

### 다중성
* 다대일
    * @ManyToOne
* 일대다
    * @OneToMany
* 일대일
    * @OneToOne
* 다대다
    * @ManyToMany

### 단방향, 양방향
* 테이블
    * 외래키 하나로 양쪽 모두에서 join이 가능하다.
    * 방향이라는 개념이 없다.
* 객체
    * 참조용 필드가 있는 쪽으로만 참조 가능
    * 한쪽만 참조하는 경우 단방향
    * 양쪽이 서로 참조하면 양방향

#### 다대일
가장 많이 사용하는 연관관계.
'다' 쪽에 FK가 위치한다.
그렇기 때문에 '다'쪽이 연관관계의 주인이 된다.

#### 일대다
일대다에서는 일이 연관관계의 주인이 되어, FK를 관리한다.
사실 별로 권장되지 않는 방법이다.

* 일대다 단방향
    * 예를 들어 Team과 Member에 대해 Team이 Member의 정보를 갖고 Member는 Team의 정보를 알고싶지 않은 것이다.
    * 그런데 생각해보면 무조건 Member에 Team에의 FK가 존재한다. 
    * 그러니까 객체의 Team을 통해 DB의 Member에 있는 FK를 관리해야 한다는 것이다.

이를 코드로 진행해보면 다음과 같다.

* Member코드
```
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
```

Member코드는 자신의 값만을 가진다.
다만 DB에서는 FK를 갖게 될 것이다.

---

* Team코드
```
@Entity
public class Team {
    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany()
    @JoinColumn(name = "TEAM_ID")
    private List<Member> members = new ArrayList<Member>();

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
```

Team코드는 member에 대해 OneToMany로 조인한다.
또 이곳에서 member에 TEAM_ID를 갖는 FK키를 생성시킨다.

---

* JpaMain코드
```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{

            Member member = new Member();
            member.setUsername("member1");

            em.persist(member);

            Team team = new Team();
            team.setName("teamA");
            team.getMembers().add(member);

            em.persist(team);

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

작성 후 실행시킨다.

---

![](https://i.imgur.com/xR3Xm99.png)
![](https://i.imgur.com/wwz0mSL.png)
![](https://i.imgur.com/nho1pVX.png)

---

![](https://i.imgur.com/OSQleSF.png)

결과를 보면 DB에 값이 제대로 들어갔음을 확인 가능하다.
그런데 Console창을 확인해 보면

1. Member값 insert
2. Team값 insert
3. Member값 update

의 순서로 진행된다.
이는 처음에는 Member에 값을 알수 없으므로 그냥 들어가고 Team이 저장된 후에 다대일에서 다시 update를 치는것이다.

성능상에 불이익이 있다.
또 로직이 뒤죽박죽 이루어져서 유지보수가 힘들수도 있다.

그래서 보통은 다대일을 기준으로 진행하다가 필요할 때 다대일+일대다로 양방향을 추가시키는쪽으로 가는것이 좋다.

> 정리

* 일대다 단방향은 일이 연관관계의 주인이다.
* 다만 항상 '다'쪽에 FK가 있다.
* 객체와 테이블 간의 차이때문에 반대편 테이블의 외래 키를 관리하게 되는 구조를 취하게 된다.
* @JoinColumn을 반드시 사용해야 한다. 그렇지 않으면 조인 테이블 방식을 사용하게 된다.
    * 뭔가 새로운 테이블을 하나 만들어서 Join용으로 사용하게 된다.
* 일대다 단방향 쓰기보다는 다대일 양방향 매핑을 사용하자.
    * 그냥 일대다 단방향 안쓰는게 나음.
* 참고로 일대다 양방향 매핑도 있기는 하다(공식적으로 존재는하지 않지만 쓸수는 있다)
    * 이거 쓸바에 다대일 양방향을 쓰자.

#### 일대일 관계
* 일대일 관계는 반대도 일대일
* 주 테이블이나 대상 테이블 중에 외래키 선택 가능
* 외래 키에 DB 유니크 제약조건 추가

거의 다대일 방식과 유사하다 생각하면 된다.
단방향의 경우 한쪽에서 Join을 걸고, 양방향의 경우 연관관계 주인의 반대쪽에서는 @OneToOne 어노테이션에서 mappedBy를 걸어주면 된다.

* 근데 만약에 외래키가 반대쪽에 있다면(마치 일대다처럼)?
    * 아예 이거는 단방향에서는 지원 자체가 안된다.
    * 양방향에서는 구현 가능하다...만 그냥 외래키 있는쪽을 연관관계 주인으로 설정하는 것이다.
  
> 정리

* 주 테이블에 외래 키 존재
    * 주 테이블에 FK를 두고 대상 테이블을 찾는다.
    * 객체지향 개발자가 선호하는 방식
    * JPA매핑이 편리하다.
    * 장점은 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 바로 확인이 가능하다.
    * 단점은 값이 없으면 외래키에 null이 허용된다.
* 대상 테이블에 외래 키 존재
    * 전통적인 DB개발자가 선호한다.
    * 장점은 주 테이블과 대상 테이블을 일대일 > 일대다로 변경할 때 편리하고, 외래키에 null이 허용되지 않는다.
    * 단점은 무조건 양방향으로 해야하고 지연 로딩으로 설정해도 항상 즉시로딩된다.
        * 주 테이블에서는 대상 테이블쪽에 값이 있는지를 확인해야 하기 때문이다. 그래서 바로 검색을 진행해버림.
            * FK가 대상테이블에만 있으므로 이 FK를 찾기위한 검색 바로 진행.

#### 다대다 관계
* 관계형 DB는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없다.
* **연결 테이블을 추가**하여 일대다, 다대일 관계로 풀어내야 한다.

- 객체는 Collection을 사용하여 객체 2개로 다대다 관계가 가능하다.


```
@ManyToMany를 통해 연결시키고
@JoinTable(name = "테이블이름")
```
을 하여 매핑해 줄 수 있다.

양방향을 하면 반대쪽에
`@ManyToMany(mappedBy = "주인에서의 값")`
으로 가능하다.

**위의 방법은 실무에서 사용하지 않는다.**
이유는
1. 연결 테이블이 단순히 연결만 해주는 것이 아니라 주문시간, 수량같은 데이터가 들어갈 수 있는데, 이 연결 리스트는 그 정보가 들어가지 않는다.
2. 쿼리의 경우도 생각치 못한 쿼리가 나타나게 될 수 있다.

실제로 다대다 관계를 구현하기 위해서는
1. @OneToMany, @ManyToOne을 하나씩 만들어 준다.
2. 연결 테이블을 하나의 엔티티로 승격시켜 준다. 즉 @JoinTable을 사용하지 않는다.
3. 그 연결 테이블에 필요한 정보를 집어넣어 주면 된다.



### 연관관계의 주인
* 테이블은 외래키 하나로 두 테이블이 연관관계를 맺는다.
* 객체는 A>B B>A 이렇게 참조가 2군데가 있다.
* 객체 양방향 관계는 참조가 양쪽에 다 있기 때문에, 외래키를 관리할 장소를 지정해야 한다.
* 즉 연관관계의 주인이란 외래키를 관리하는 참조이다.
    * 연관관계 주인의 반대는 외래키에 영향을 주지 않고, 조회만 가능하다.

---


## 상속관계 매핑
객체는 상속관계가 있지만, 관계형DB는 상속 관계가 없다.
슈퍼타입-서브타입 관계가 객체 상속과 유사하다.

여기서 상속관계 매핑이란 객체의 상속과 구조와 DB의 슈퍼타입 서브타입 관계를 매핑하는 것이다.

* 슈퍼타입-서브타입 논리 모델을 실제 물리 모델로 구현하는 방법
    * 각각 테이블로 변환 -> 조인 전략
    * 통합 테이블로 변환 -> 단일 테이블 전략
    * 서브타입 테비을로 변환 -> 구현 클래스마다 테이블 전략

![](https://i.imgur.com/1FbS1hP.png)

먼저 다음과 같은 논리 모델이 있다고 가정한다.

### 조인 전략
Item이라는 테이블을 만든 후, ALBUM MOVIE BOOK 테이블을 각각 만들어 준다.
그리고 값을 넣을 때 join을 통해 값을 넣어주게 한다.

![](https://i.imgur.com/XIFyRNx.png)

이런 식으로 값들을 나누어서 만들어 두고 각각 필요한 곳에 insert한다. join은 ITEM의 PK를 각각 테이블에서 PK이자 FK로 구성하여 진행한다.
그리고 조인 도중 상태 테이블 구분을 위해 DTYPE을 만들어 준다.

코드 작성하기

* Item 클래스 생성

```
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 조인 전략으로 설정하기
@DiscriminatorColumn
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
```

abstract로 만들어 준 이유는 해당 Item은 단일로 사용되는 것이 아니라 슈퍼타입으로만 쓰이기 때문이다.

Inheritance의 전략을 InheritanceType.JOINED로 하면 join 전략으로 진행한다.
@DiscriminatorColumn 를 사용하여 DTYPE의 구현이 가능하다.
-> 추가로 만약 DTYPE에서 들어가는 Value를 설정하고 싶으면 자식 클래스에서 @DiscriminatorValue("설정할이름") 를 통해 설정 가능하다.

---

* Album클래스 생성

```
@Entity
public class Album extends Item{
    private String artist;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
```

---

* Movie클래스 생성

```
@Entity
public class Movie extends Item {
    private String director;
    private String actor;

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }
}
```

---

* Book클래스 생성

```

@Entity
public class Book extends Item{
    private String author;
    private String isbn;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
```

---

* JpaMain 작성

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{

            Movie movie = new Movie();
            movie.setDirector("aaaa");
            movie.setActor("bbbb");
            movie.setName("바함사");
            movie.setPrice(10000);

            em.persist(movie);

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

![](https://i.imgur.com/s7fIH8W.png)
![](https://i.imgur.com/5JW2Lf8.png)


원하는 구성이 제대로 이루어졌음을 확인 가능하다.

이제 이를 조회해보면

* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{

            Movie movie = new Movie();
            movie.setDirector("aaaa");
            movie.setActor("bbbb");
            movie.setName("바함사");
            movie.setPrice(10000);

            em.persist(movie);

            em.flush();
            em.clear();

            Movie findMovie = em.find(Movie.class, movie.getId());
            System.out.println("findMovie" + findMovie);

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이렇게 캐시를 지우고 값을 보낸 뒤 확인해 보면

![](https://i.imgur.com/puNO39n.png)


이렇게 join하여 값을 가져오게 되는 것을 확인할 수 있다.

* 장점
    * 데이터가 정규화되어 들어간다.
    * 외래키 참조 무결성 제약조건의 활용이 가능하다.
    * 저장공간을 효율적으로 관리 가능하다.
* 단점
    * 조회시 조인을 많이 사용하여 성능이 저하된다.
    * 조회 쿼리가 복잡해진다.
    * 데이터 저장시 Insert SQL이 2번 호출된다


### 단일 테이블 전략
![](https://i.imgur.com/tvTuvYa.png)

하나의 테이블에서 싹다 처리한다.

이 전략으로 바꾸기 위해서는 위의 코드에서 Item에 있는 전략을 바꾸어 주기만 하면 된다.

* Item 클래스에서
`@Inheritance(strategy = InheritanceType.SINGLE_TABLE)`

으로 변경하고 실행해 주면

![](https://i.imgur.com/2F7D0VP.png)

Movie, Book, Album과 같은 테이블들은 따로 생성되지 않고, 모든 값들을 가진 SINGLE_TABLE하나가 생성된다.

![](https://i.imgur.com/bku9y6M.png)
![](https://i.imgur.com/rUcqjph.png)

insert와 select도 한번에 진행된다.

참고로 SINGLE_TABLE전략은 @DiscriminatorColumn 코드가 따로 없어도 알아서 DTYPE을 구현해 준다.
이게 없으면 각각의 차이를 알 수 없기 때문.

여기서 뭔가 이상한점이 있다.
DB를 확인해 보면 ITEM이라는 Table에 모든 값들이 존재하고 이를 상속받는 테이블들은 실제로 존재하지 않는다.
근데 JpaMain코드에는 각각의 객체들은 존재하고, 이를 통해 값을 세팅해주고 있다.

이게 JPA의 장점 중 하나인데, 전략만 바꾸고 코드를 바꿀 필요 없이 바로 변경해준다.

* 장점
    * 조인이 없어서 조회 성능이 빠르다.
    * 조희 쿼리가 단순하다.
* 단점
    * 자식 엔티티가 매핑한 컬럼은 모두 null허용
        * 무결성에 문제가 생길 수 있다.
    * 단일 테이블에 모든것을 저장해서 테이블이 커질 수 있다.
    * 상황에 따라 조회 성능이 떨어질 수도 있다.


### 구현 클래스마다 테이블 전략
![](https://i.imgur.com/IjC5Zjz.png)

테이블을 구현할 때 각각의 테이블이 모든 정보를 가지고 있도록 한다.

* Item 클래스에서
`@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)`

로 설정해 주면 된다.
위의 단일 테이블과는 반대로 여기서는 ITEM 테이블이 구현되지 않고, Movie Book Album과 같은 테이블들이 각각의 값을 모두 가지고 구현되게 된다.

![Uploading file..._7wfrnu0l4]()
![](https://i.imgur.com/nmBH932.png)

이런 결과를 갖게 될 것이다.

이 전략은 큰 결점을 갖고 있는데 만약 존재하지 않는 Item을 통해 검색을 진행하는 경우
UNION을 통해 모든 Movie Book Album을 통합시켜서 거기서 검색을 진행하게 된다.
이 경우 매우 비효율적이고 복잡해진다.
-> 이 전략은 그냥 쓰지 말자 

* 장점
    * 서브 타입을 명활하게 구분해서 처리할 때 효과적
        * insert같은거 할때는 효과적이다.
    * not null 제약조건의 사용이 가능하다.
* 단점
    * 여러 자식 테이블을 함께 조회할 때 성능이 느림(UNION SQL)
    * 자식 테이블을 통합해서 쿼리하기 어렵다.

---

## @MappedSuperclass

* 상속관계 매핑이 아니다!
* 엔티티X, 테이블과 매핑X
* 부모 클래스를 상속받는 자식 클래스에 매핑 정보만을 제공해 준다.
* 조회나 검색이 불가능하다.
* 어차피 생성되지도 않으니 추상 클래스를 사용하자!
* 참고로 Entity클래스는 이 클래스나 @Entity클래스만 상속 가능하다.

![](https://i.imgur.com/WUzkVFt.png)

DB는 관계 없이 객체 입장에서 공통되는 것들을 그냥 상속받아 쓰고싶을 때 사용하는 것이다.
class 상속을 생각하면 될 것 같다.

구현해 보자면

* BaseEntity 생성
```
@MappedSuperclass       // 매핑 정보만 받는 superclass가 된다.
public abstract class BaseEntity {

    private String createBy;
    private LocalDateTime createDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
```

@MappedSuperclass 를 사용하여 이 클래스는 단순히 superclass로 기능하게끔 한다.

---

* Member클래스와 Team클래스에서 이 BaseEntity를 extends해준다.

---

* JpaMain 수정
```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{

            Member member = new Member();
            member.setUsername("user1");
            member.setCreateBy("kim");
            member.setCreateDate(LocalDateTime.now());

            em.persist(member);

            em.flush();
            em.clear();

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이렇게 실행해 주면

![](https://i.imgur.com/HVqpF3h.png)
![](https://i.imgur.com/8SQD18x.png)

변환된 DB가 생성되고 
![](https://i.imgur.com/t7jCiXU.png)
![](https://i.imgur.com/dUeV8VY.png)

값도 잘 들어가는 것이 확인된다.

---

## 프록시
JPA에서는 em.find()말고도 em.getReference()라는 메소드를 제공한다.

이 em.getReference()는 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체를 조회한다.
-> 즉 DB에 쿼리가 나가지 않은 상태로 객체를 조회한다.

코드를 통해 확인해 보자면

* Member클래스에서 필요없는거 없애기

```
@Entity
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
```

---

* Locker 클래스 삭제하기

---

* JpaMain 클래스 변경

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = new Member();
            member.setUsername("hello");

            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.getReference(Member.class, member.getId());

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이렇게 하면 findMember은 getReference를 실행하지만, 실제로 이게 쿼리를 조회하지는 않는다.

![](https://i.imgur.com/l9sgFz8.png)

이런 식으로 insert만 하고 종료됨을 확인 가능하다.

---

* JpaMain 변경

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = new Member();
            member.setUsername("hello");

            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.getReference(Member.class, member.getId());

            System.out.println(findMember.getId());
            System.out.println(findMember.getUsername());

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이렇게 처음 설정해준 Id외의 Username값을 가져오게 되면

![](https://i.imgur.com/3sR1c4T.png)

그 Username을 가져오려 할 때 select 쿼리가 날라가는 것을 확인할 수 있다.

---

저 getReference로 만들어지는 클래스는 ProxyClass라는 가짜 클래스이다.

### 프록시의 특징
* 실제 클래스를 상속받아 만들어진다.
* 실제 클래스와 겉모양이 같다.
* 사용자 입장에서는 이제 진짜 객체인지 프록시 객체인지 굳이 구분 없이 사용하면 된다.(이론상)
* 프록시 객체는 실제 객체의 참조를 보관한다.
    * 그렇게 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메소드를 호출한다.
    ![](https://i.imgur.com/fWPeRQt.png)
    1. 프록시 객체 가져오기
    2. getName으로 이 이름 가져오기
    3. 프록시 객체에는 이 getName이 없다.
    4. 영속성 컨텍스트에 초기화 요청함.(진짜 값을 만들어내는 것을 초기화라 한다.)
    5. DB를 조회해서 실제 엔티티를 생성하고, 여기에 target을 이용하여 연결해준다.
    6. 프록시 객체에서 target으로 실제 entity의 값을 가져와서 전달해준다.
* 프록시 객체는 처음 사용할 때에 한 번만 초기화해준다.
* 프록시 객체를 초기화 하는것이, 실제 엔티티로 바뀌는 것은 아니다. 단지 실제 엔티티에 접근 가능하도록 하는 것이다.
* 프록시 객체는 원본 엔티티를 상속받는다.
    * **타입 체크시 주의해야한다.** (==비교 실패, 대신 instance of를 사용할것.)
        * 하나는 프록시에서 하나는 entity에서 받아오면 다를수 있으니
            * 근데 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 entity가 반환됨.
                * 이미 영속성 컨텍스트가 있는데 굳이 새거를 만들 필요가 없기도 하고
                * JPA가 한 트랜잭션 내에서 동작하는 것에 대해 == 동일성을 보장해 준다. 그를 위해 이미 있는 것에 대해 만들어지면 동일 엔티티로 만들어 주는 것이다.
            * 위와는 반대로 em.getReference()를 통해 프록시를 만들어내고, 이후 em.find()를 진행하게 되면 em.find()로 만들어진 것이 엔티티가 아니라 프록시가 된다.
                * 이 또한 위에서와 마찬가지로 JPA에서 == 비교를 맞추어 주기 위해 엔티티를 프록시로 만들어 주게 되기 때문이다.
* 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일때 프록시 사용하면 문제가 발생한다.(예외터짐)
    * detach하거나 close하거나 clear하거나~

### 프록시 확인
* 프록시 인스턴스의 초기화 여부 확인
    * (emf에서).PersistenceUnitUtil.isLoaded(Object entity)
        * 그니까 상속받은 엔티티가 있는지를 확인해주는거
* 프록시 클래스 확인
    * entity.getClass()
        * 그러면 클래스 타입(HibernateProxy인지 이런거)이 나온다.
* 프록시 강제 초기화
    * Hibernate.initialize(entity)
        * 강제로 알아서 초기화됨.
            * 참고로 JPA표준에는 강제 초기화 없으므로 걍 호출하면 초기화 됨.

---


## 즉시 로딩과 지연 로딩

### 지연 로딩 LAZY
fetchType을 LAZY로 설정하면, 지연로딩의 대상이 된 쪽은 프록시로 가져온다.
그리고 이후에 그 값을 실제로 사용할 때에 초기화가 진행된다.

@ManyToMany, @OneToMany는 기본이 LAZY로 되어 있다.


### 즉시 로딩 EAGER
fetchType을 EAGER로 설정하면, 로딩할 때에 모든 값을 join하여 가져온다.
그렇기 때문에 이렇게 가져오면 프록시로 가져오지 않고 실제 엔티티를 가져온다.

아니면 select 쿼리를 두 번 날려서 가져올수도 있다.

* 다만 실무에서는 **가급적 지연 로딩만 사용**한다.
    * 즉시 로딩을 사용하면 전혀 예상하지 못한 SQL이 나타날 확률이 높다.
        * 그리고 join이 여러개 걸리면 엄청나게 문제가 생긴다...(join은 지수함수로 증가해서)
    * 즉시로딩은 JPQL에서 N+1문제를 일으킨다.
        * JPQL은 JPA와는 다르게 최적화가 되어있는것이 아닌 변환해주는 내용이다.
            * 만약 EAGER로 되어있는 값을 갖는 엔티티를 
            `select m from member m`
              이렇게 검색하면
                * member를 select -> Member에 EAGER로 되어있는 모든 값들을 바로 select ... 
                    * 이렇게 값의 개수만큼 검색해버린다.
            * LAZY로 하면 일단 바로 검색은 안하기 때문에 필요할 때만 select가 나간다.
                * 나중에는 이걸 해결하기 위해 fetch join / Entity Graph를 사용하면 된다.

@OneToOne, @ManyToOne은 기본이 EAGER로 되어 있다.
이를 LAZY로 설정하여 바꾸자

#### 활용
* Member와 Team을 자주 함께 사용 -> 즉시 로딩
* Member와 Order는 가끔 항께 사용 -> 지연 로딩
* Order와 Product는 자주 함께 사용 -> 즉시 로딩

이긴 한데....실무에서는 그냥 싹다 지연로딩 할것. 

---

## 영속성 전이(Cascade)
특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속상태로 만들고 싶을때
부모에서 Cascade하고 나서 영속화 하면 그 자식들도 영속화가 되는것이다.

예를 들어

* Parent Entity생성
```
@Entity
public class Parent {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child){
        childList.add(child);
        child.setParent(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

이곳은 부모엔티티로, 여러 child엔티티를 자식으로 가진다.
Cascade를 설정하여 부모가 영속화되면 이게 자식에도 바로 적용될 수 있도록 한다.

---

* Child Entity 생성

```
@Entity
public class Child {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }
}
```

---

* JpaMain 수정
```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();

            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

여기서 사용한 addChild는 부모에서 따로 적용해준 연관관계 편의 메소드이다.
바로 child에도 값을 세팅해주도록.

영속화는 Parent에만 적용되게 된다.
그런데 cascade로 하였기 때문에 바로 자식에도 영속화가 진행될 것이다.

실행해 보면

---

![](https://i.imgur.com/ePdTaSq.png)
![](https://i.imgur.com/bIB70YY.png)

잘 들어갔다.

---


### Cascade 주의사항
* 영속성 전이는 연관관계 매핑과 아무런 관련이 없다!!
* 그냥 하나의 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 것 뿐이다.

### Cascade 종류
    - ALL
        - 모두 적용
    - PERSIST
        - 영속
    - REMOVE
        - 삭제
    - MERGE
        - 병합
    - REFRECH
        - 리프레시
    - DETACH
        - 준영속
주로 이 종류에서 ALL, PERSIST, REMOVE정도가 쓰인다.

그래서 주로 언제 쓰이나??
게시판이나 첨부파일 같은경우에 쓰인다.
그 부모에서만 관리하는 경우.
근데 이제 이 엔티티를 여러 장소에서 관리하는 경우에는 쓰지 말자. 잘못하면 어디서 써야하는데 지워버림

즉 완전히 단일 부모에 종속적인 경우에 사용하도록 하자.

---

## 고아 객체
* 고아 객체 제거
    * 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제함
    * orpanRemoval = true
    * 즉 어떤 부모와의 연관관계가 끊어지면 자식을 바로 없애준다.

코드로 보자면

* Parent

```
@Entity
public class Parent {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child){
        childList.add(child);
        child.setParent(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }
}
```

Parent에서 다음과 같이 orphanRemoval을 true로 설정해 준다.

---

* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();

            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            findParent.getChildList().remove(0);

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이렇게 orphanRemoval을 true로 설정해 준 childList Collection에서 값을 삭제하여 Parent와 해당 Child의 연관이 끊기면 child가 바로 삭제될 것이다.

실행해 보면

---

![](https://i.imgur.com/iOFGoCu.png)
![](https://i.imgur.com/KVEJfuT.png)

잘 지워진 것을 확인 가능하다.

---

그리고 당연하겠지만 부모 전체가 삭제되면 자식도 삭제된다.
이는 마치 CascadeType.REMOVE와 비슷하게 동작한다.

---

## 영속성 전이 + 고아객체, 생명주기
CascadeType.ALL + orphanRemoval=true
* 스스로 생명주기를 관리하는 엔티티는 em.persist()로 영속화, em.remove()로 제거
* 두 옵션을 모두 활성화 하면 부모 엔티티를 통해 자식의 생명주기를 관리할 수 있다.
    * 즉 Parent를 통하기만 해도 Child를 관리 가능.
        * **도메인주도설계(DDD)의 Aggregate Root개념을 구현할 때 유용**하다
            * Aggregate Root에서만 리포지토리와 컨텍하고 나머지에서는 리포지토리를 만들지 않는것이 좋다.

---


## 값 타입
* 기본값 타입
* 임베디드 타입(복합 값 타입)
* 값 타입과 불변 객체
* 값 타입의 비교
* 값 타입 컬렉션

### 기본값 타입
JPA는 데이터 타입을 최상위 레벨로 볼 때 두가지로 분류한다.
* 엔티티 타입
    * @Entity로 정의하는 객체
    * 데이터가 변해도 식별자를 통해 지속해서 **추적이 가능**하다.
* 값 타입
    * int, Integer, String처럼 단순히 값으로 사용하는 자바 기본 타입이나 객체
    * 식별자가 없고 값만 있으므로 변경시 추적이 불가능하다.
    
#### 값 타입의 분류
* 기본값 타입
    * 자바 기본 타입(int, double)
    * 래퍼 클래스(Integer, Long)
    * String
* 임베디드 타입(enbedded type, 복합 값 타입)
* 컬렉션 타입

##### 기본값 타입
* 생명 주기를 엔티티에 의존한다.
    * 즉 엔티티를 삭제하면 기본값도 삭제된다.
* 값 타입은 공유하면 안된다.
    * 예를 들어 회원 이름 변경시 다른 회원의 이름도 함께 변경되면 안된다.

### 임베디드 타입(복합 값 타입)
* 새로운 값 타입을 직접 정의할 수 있다.
* JPA는 임베디드 타입이라 함
* 주로 기본 값 타일을 모아 만들어서 복합 값 타입이라고도 함
* int, String과 같은 값 타입
    * 그러니까 이것도 엔티티가 아닌 값 타입이라 추적이 안된다.

예를 들어 회원 엔티티가 다음과 같을 때
![](https://i.imgur.com/nCzKHbu.png)

![](https://i.imgur.com/vxKtKdV.png)
이렇게 추상화하여 사용할 수 있다.

여기서 기간 / 주소와 같은 내용을 한꺼번에 묶어 임베디드 타입으로 나타내는 것이다.

* 장점
    * 재사용 가능
    * 높은 응집도를 갖는다.(서로 연관도가 높은 것들로 이루어졌기 때문에)
    * 해당 값 타입만 사용하는 의미있는 메소드를 만들어 낼 수 있다.
        * 예를들어 workDay 내에 period.isWork()와 같이 일하는 기간을 산정하는 타입을 정의 가능
    * 값 타입이기 때문에, 이것을 소유한 엔티티에 생명 주기를 의존한다.

Table입장에서는 임베디드 타입을 사용하여도 딱히 내용이 변하지는 않는다.

---

코드를 통해 적용 내용을 확인하자면

* Member 클래스
```
@Entity
public class Member{

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    // 기간 period
    @Embedded
    private Period workPeriod;

    @Embedded
    private Address homeAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }
}
```

---

* Period 클래스 생성

```
@Embeddable
public class Period {
    private LocalDateTime startData;
    private LocalDateTime endDate;

    public LocalDateTime getStartData() {
        return startData;
    }

    public void setStartData(LocalDateTime startData) {
        this.startData = startData;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
```

---

* Address 클래스 생성
```
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;

    public Address(){};

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
```

--- 

* JpaMain 변경

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{

            Member member = new Member();
            member.setUsername("hello");
            member.setHomeAddress(new Address("city", "street", "100"));
            member.setWorkPeriod(new Period());

            em.persist(member);

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```


--- 

이렇게 설정하고 실행하면

![](https://i.imgur.com/m9ARf5X.png)
![](https://i.imgur.com/w2Sw3SU.png)

잘 동작하는 것을 확인할 수 있다.

---

추가로 동일한 Enbedded 타입을 사용하여 다른 내용을 표현하고 싶다면
* @AttributeOverrides, @AttributeOverride를 사용하면 된다.

예를 들어 집주소, 회사주소를 각각 매핑하고 싶다면

* Member

```
@Entity
public class Member{

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    // 기간 period
    @Embedded
    private Period workPeriod;

    @Embedded
    private Address homeAddress;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name="city", column=@Column(name = "WORK_CITY"))
                        , @AttributeOverride(name="street", column=@Column(name = "WORK_STREET"))
                        , @AttributeOverride(name="zipcode", column = @Column(name = "WORK_ZIPCODE"))
    })
    private Address workAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }
}
```

이렇게 해주면 된다.

---

### 값 타입과 불변 객체
값 타입은 객체를 단순하고 안전하게 다룰 수 있어야 한다.

#### 값 타입 공유 참조
* 임베디드 타입같은 값 타입을 여러 엔티티에서 공유하면 위험하다.
![](https://i.imgur.com/7XcbfrW.png)

---

예를 들어
* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Address address = new Address("city", "street", "10000");

            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(address);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setHomeAddress(address);
            em.persist(member2);

            member.getHomeAddress().setCity("newCity");

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이렇게 실행하면 본래 member에서만 City의 값을 newCity로 변경하려 하는 것인데, 실제로 실행해 보면

![](https://i.imgur.com/iVrdAja.png)
![](https://i.imgur.com/bH56ipu.png)

두 값 모두가 변경되는 것을 확인할 수 있다.

-> 이걸 막기 위해서는 값을 복사해서 사용하도록 한다.
```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Address address = new Address("city", "street", "10000");

            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(address);
            em.persist(member);

            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());
            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setHomeAddress(copyAddress);
            em.persist(member2);

            member.getHomeAddress().setCity("newCity");

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이런 식으로

---

그 이유는
* 객체 타입의 한계
    * 항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용을 피할 수 있다.
        * 임베디드 타입과 같이 **직접 정의한 값 타입은 자바의 기본타입이 아닌 객체 타입**이다.
    * 자바 기본 타입에 값을 대입하면 값을 복사한다.
        * 객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없다.
    * 객체의 공유 참조는 피할 수 없다.
    
#### 불변 객체
* 위의 부작용을 막기 위해서는 객체 타입을 수정할 수 없게 만들어버리면 된다.
* 그러므로 **값 타입은 불변객체로 설계**해야한다.
    * 불변 객체란 생성 이후 값을 절대 변경할 수 없는 객체이다.
* 즉, 생성자로만 값을 설정하고 수정자를 만들지 않도록 한다.
* 참고로 Integer, String은 자바가 제공하는 대표적인 불변 객체이다.

### 값 타입 비교
값 타입은 인스턴스가 달라도, 그 안에 값이 같으면 같은 것으로 본다.
* 동일성(identity) 비교
    * 인스턴스의 **참조값**을 비교 (==)
* 동등성(equivalence) 비교
    * 인스턴스의 **값**을 비교 (equals())

* 값 타입은 a.equals(b)를 사용하여 동등성 비교를 해야 한다.
* 값 타입의 equals()메소드를 적절히 재정의하여 사용(주로 모든 필드 사용)
    * 추가로 이 equals는 기본이 ==이다. 
        * 그렇기 때문에 재정의해야 하고, 그 코드는 아래와 같다.

---

* Address

```
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;

    public Address(){};

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) && Objects.equals(street, address.street) && Objects.equals(zipcode, address.zipcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, zipcode);
    }
}
```

---

### 값 타입 컬렉션
값 타입을 컬렉션에 담아서 사용하는 것.

이 때 문제는, 이를 RDB에 저장할 때에 Table에 저장할 마땅한 방법이 없다는 것이다.
이를 해결하기위해서는 별도의 Table을 새로 만들어서 받아야 한다.

![](https://i.imgur.com/jLhWihM.png)

이런 식으로 진행.

-> 모든 값들을 묶어서 하나의 PK로 만들어내야 한다. 그렇지 않고 식별자 ID같은 개념을 도입시키면 Entity가 되는 것이다.

살펴보자면

---

* Member코드 수정

```
@Entity
public class Member{

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Embedded
    private Address homeAddress;

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME")     // Collection의 안에 따로 다른 컬럼이 없는 경우(String만 존재) 사용 가능하다.
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    private List<Address> addressHistory = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }
}
```

@ElementCollection을 사용하여 매핑을 해준다.
@CollectionTable을 사용하면 그 테이블의 이름을 지정해 줄 수 있고, joinColumns를 사용하여 join해 줄 컬럼을 지정한다.

실행해 보면

---

![](https://i.imgur.com/Se9dGIJ.png)
![](https://i.imgur.com/eVrNIXr.png)
![](https://i.imgur.com/2CNCjN1.png)

이렇게 테이블이 생성된다.

---

이러한 값 타입 컬렉션은
* 값 타입을 하나 이상 저장할 때 사용
* DB는 컬렉션을 같은 테이블에 저장할 수 없다.
    * 일대다 개념이기 때문에
* 그렇게 컬렉션을 저장하기 위한 별도의 테이블을 필요로 한다.

---

#### 값타입 컬렉션 저장

* JpaMain
 
```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = new Member();
            member.setUsername("member");
            member.setHomeAddress(new Address("homeCity", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("초밥");
            member.getFavoriteFoods().add("삼겹살");

            member.getAddressHistory().add(new Address("old1", "street", "10000"));
            member.getAddressHistory().add(new Address("old2", "street", "10000"));

            em.persist(member);

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이렇게 작성하고 실행하면
![](https://i.imgur.com/edbiu2S.png)

이렇게 한번의 persist로 모든 값들이 만들어지는 것을 확인할 수 있다.

이는 값타입 컬렉션이 스스로 생명주기를 가진 것이 아니라, 주인이 되는 엔티티와 생명주기를 공유하기 때문이다.

따라서 만약 이 주인의 값타입이 변경된다면 아래의 값타입 컬렉션의 값이 변경될 것이다.

마치 cascade / orphanremoval을 켜준것과 비슷한 느낌이다.

---

#### 값타입 컬렉션 조회

이 값타입 컬렉션은 지연 로딩으로 조회된다.

* JpaMain 수정

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = new Member();
            member.setUsername("member");
            member.setHomeAddress(new Address("homeCity", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("초밥");
            member.getFavoriteFoods().add("삼겹살");

            member.getAddressHistory().add(new Address("old1", "street", "10000"));
            member.getAddressHistory().add(new Address("old2", "street", "10000"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("============== start ===============");
            Member findMember = em.find(Member.class, member.getId());

            List<Address> addressHistory = findMember.getAddressHistory();
            for(Address address : addressHistory) System.out.println("address = " + address.getCity());
            tx.commit();

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for(String favoriteFood : favoriteFoods) System.out.println("favoriteFood = " + favoriteFood);

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이런 식으로 설정하고 실행해 보면

![](https://i.imgur.com/mxWOxel.png)
![](https://i.imgur.com/ZpDcheE.png)

이렇게, 먼저 부모 엔티티를 호출하고 이후에 값을 요구할 때에 따로 조회하는 것을 확인 가능하다.

이는 @ElementCollection의 기본 fetch가 지연 로딩으로 되어있기 때문이다.

---

#### 값타입 컬렉션 수정

먼저 하나의 인자를 갖는 값타입 컬렉션을 수정하려 한다면

* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = new Member();
            member.setUsername("member");
            member.setHomeAddress(new Address("homeCity", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("초밥");
            member.getFavoriteFoods().add("삼겹살");

            member.getAddressHistory().add(new Address("old1", "street", "10000"));
            member.getAddressHistory().add(new Address("old2", "street", "10000"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("============== start ===============");
            Member findMember = em.find(Member.class, member.getId());

            // 하나의 값타입을 삭제하고 이후에 새로 추가
            // 업데이트 자체가 안됨 이거.
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("순살치킨");

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이렇게 하는 수밖에 없다.
값타입의 수정은 불가능하고 새로 추가하고 삭제하여야 한다.

---

![](https://i.imgur.com/gDeQF05.png)
![](https://i.imgur.com/2R9Pr5y.png)

---

그리고 여러 인자를 갖는 컬렉션 값타입을 수정하려 한다면 이전의 값 비교에서 만든 동등성비교/Hash가 중요해진다.
위에서 확인하듯 여기서 수정은 실제로 update를 하는 것이 아니라 해당 객체를 삭제 후 새로 insert하는 것이기 때문이다.

* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = new Member();
            member.setUsername("member");
            member.setHomeAddress(new Address("homeCity", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("초밥");
            member.getFavoriteFoods().add("삼겹살");

            member.getAddressHistory().add(new Address("old1", "street", "10000"));
            member.getAddressHistory().add(new Address("old2", "street", "10000"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("============== start ===============");
            Member findMember = em.find(Member.class, member.getId());

            // 기본적으로 컬렉션은 대부분 대상을 찾을 때 equals를 사용한다.
            // 완전히 동일한 값을 사용하는 대상을 찾아간다.
            // 따라서 이전에 값 비교에서 만든 equals, hashcode가 제대로 구성되어 있어야 한다.
            findMember.getAddressHistory().remove(new Address("old1", "street", "10000"));
            findMember.getAddressHistory().add(new Address("newCity1", "street", "10000"));

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이렇게 equals를 사용하여 비교한 뒤, 동등성을 갖는 객체라면 삭제하고 추가해 준다.

---

![](https://i.imgur.com/lgH3l3S.png)
![](https://i.imgur.com/ZvnARAC.png)

이렇게 된다.

그런데 여기서 console에 출력된 내용을 확인해 보면
1. 해당 Member에 존재하는 모든 Address 컬렉션 값 타입 삭제
2. 다시 처음부터 insert

이렇게 된다.

#### 값 타입 컬렉션 제약사항
* 값 타입은 엔티티와 다르게 식별자 개념이 없다.
    * 따라서 값을 변경하면 추적이 어렵다.
* 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 현재 값을 모두 저장한다.
    * 이것 때문에 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본키를 구성해야한다.
        * 이렇게 하면 null입력, 중복 저장이 안되기 때문이다.

@OrderColumn을 사용하면 해당하는 위치의 PK를 다시 잡아주기 때문에 이걸 사용하면 해결 가능하지만...그냥 쓰지 말자 

#### 값 타입 컬렉션 대안
* 실무에서는 상황에 따라 값 타입 컬렉션 대신 일대다 관계를 고려한다.
    * 실별자가 필요하고, 지속해서 값을 추적/변경해야 한다면 그건 값타입이 아닌 엔티티이다.
* 값 타입 이거는 제한된 상황에서만 쓰는것이 좋다.
    * 예를 들어 1, 2, 3번 옵션을 주고 여기서 선택하도록 할때

---

## 객체지향 쿼리 언어(JPQL)
JPA는 다양한 쿼리 방법을 지원한다.
* JPQL
    * Java 코드로 짜서, 이를 SQL로 빌드해주는 것
* QueryDSL
    * Java 코드로 짜서, 이를 SQL로 빌드해주는 것
* 네이티브 SQL
    * 완전히 특정DB에 종속되는 쿼리를 사용해야 할 때에, 이를 사용한다.
    * 생쿼리
* JPA Criteria
* JDBC API 직접사용, MyBatis, SpringJdbcTemplate등등...

### JPQL
* EntityManager.find()
* 객체 그래프 탐색(a.getB().getC()) 등등..

만약 여기서 나이가 18세 이상인 데이터를 가져오고 싶다면?

#### JPQL의 등장 배경
- JPA를 사용하면 엔티티 중심으로 개발한다.
- 문제는 검색 쿼리이다.
- 검색을 할 때에도 Table이 아닌, Entity대상으로 검색해야한다.
- 모든 DB데이터를 객체로 변환해서 검색하는것은 실질 불가능하다.
- 그렇기 때문에 애플리케이션이 필요한 데이터만 DB에서 볼러오려면 결국 검색 조건이 포함된 SQL이 필요할 것이다.

#### JPQL 특징
* JPA는 SQL을 추상화한 JPQL이라는 **객체지향쿼리언어**를 제공한다.
* SQL과 문법이 유사하다.
* 엔티티 객체를 대상으로 쿼리한다.
    * 이는 DB Table을 대상으로 쿼리하는 SQL과의 가장 큰 차이점이다.
* SQL을 추상화하여 특정 DB SQL에 의존하지 않는다.

---

* JpaMain
```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{

            List<Member> result = em.createQuery("select m From Member m where m.username like '%kim%'"
                        , Member.class)
                        .getResultList();

            tx.commit();

        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

예를 들어 다음과 같은 코드를 실행하면
![](https://i.imgur.com/BNseTd0.png)

이렇게 번역되어 실행되는데, 이 process는
1. Entity를 대상으로 쿼리하면
2. 이 Entity에의 매핑 정보를 읽어
3. 적절한 SQL을 만들어낸다.

---

### Criteria
기존의 JPQL은 동적 쿼리의 생성이 어렵다.
예를 들어 값이 null이 아닌 경우 where문 실행과 같은 경우

if(값 != null) String += "where ~~~~ "
이렇게 해야한다.

이런 동적 쿼리의 해결을 위한 방법이다.

코드를 예를 들어

---

* JpaMain
```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            Root<Member> m = query.from(Member.class);

            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
            
            List<Member> resultList = em.createQuery(cq).getResultList();

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이렇게 하면 Criteria가 자바 문법처럼 확인하도록 나타내 준다.

![](https://i.imgur.com/fIeONXt.png)

이런 식으로

---

이렇게 Criteria로 사용하면 
* 문자가 아닌 자바코드로 JPQL을 작성할 수 있다.
* 쿼리 생성 도중 컴파일 오류를 바로 잡아줌
* 동적 쿼리 생성시 기존의 JPA보다 편하게 생성 가능하다.
* 다만 이게 사실 뭔가 쿼리같은 느낌이 들지가 않는다...
    * 그렇기 때문에 유지보수가 어려워 실무에서는 잘 사용하지 않는다.
        * 거의 반쯤 망한 방법이라 함.

이 Criteria대신에 훨씬 좋은 방법이 **QueryDSL**이다.

---

### QueryDSL
오픈소스임.
* 문자가 아닌 자바코드로 JPQL을 작성 가능하다.
* JPQL빌더 역할
* 컴파일 시점에 문법 오류를 찾을 수 있다.
* 동적쿼리 작성이 편리하다.
* 단순하고 쉽다.
* 실무에서 사용할때는 이걸 쓰자

---

### Native SQL
* JPA가 제공하는 SQL을 직접 사용하느 기능
* JPQL로 해결할 수 없는 특정 DB에 의존적인 기능
    * CONNECT BY 등등..
* 생쿼리를 집어넣으면 된다.

---

## JDBC직접사용, SpringJcbcTemplate 등
* JPA를 사용하면서 JDBC커넥션을 직접 사용하거나, 스프링 JdbcTemplate, 마이바티스 등을 함께 사용 가능하다.
* 단 이를 사용하면 JPA와 실질적인 관련은 없는 것이다.
    * 따라서 영속성 컨텍스트 사용 도중 적절한 타이밍에 강제로 flush해주어야 한다.
        * flush는 기본적으로 commit할 때나 쿼리를 날릴 때에 동작하게 되는데, 여기서는 우회하여 실행하기 때문에 이것이 동작하지 않기 때문

---

간단하게 사용하자면
1. JPQL
2. QueryDSL

이 두개를 선택하여 사용하도록 하자

---


## JPQL(Java Persistence Query Language)

### JPQL 기본 문법과 기능
* JPQL은 객체지향 쿼리 언어이다.
    * 테이블 대상이 아닌, 엔티티 객체를 대상으로 쿼리한다.
* SQL을 추상화해서 특정 DB sql에 의존하지 않는다.
* 결국 SQL로 변환된다.

#### JPQL 문법
* select m from Member as m where m.age > 19
    * 엔티티와 속성은 대소문자를 구분한다(Member, age)
    * JPQL키워드는 대소문자를 구분하지 않는다.(select, from, where ... )
    * 엔티티 이름을 사용한다(테이블이름이 아니라)
    * 별칭은 필수이다.(m)

##### TypeQuery, Query
* TypeQuery
    * 반환 타입이 명확할 때 사용
* Query
    * 반환 타입이 명확하지 않을 때 사용

예를 들어

* Member

```
@Entity
public class Member {
    @Id @GeneratedValue
    private Long id;

    private String username;
    private int age;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

이런 Member 클래스에서

---

```
* JpaMain

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> query = em.createQuery("select m from Member as m", Member.class);
            Query query2 = em.createQuery("select m.username, m.age from Member as m");

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

TypedQuery를 통해 쿼리를 돌렸을 때 명확한 타입을 알고있으면 그 타입을 뒤에 Member.class 이런식으로 매핑해주고, 받을 수 있다.

명확한 타입을 모르는 경우는 Query를 사용한다.

---

##### 결과 조회 API
* query.getResultList()
    * 결과가 하나 이상일 때, 리스트 반환
        * **결과가 없으면 빈 리스트 반환**
* query.getSingleResult()
    * 결과가 정확히 하나, 단일 객체 반환
        * 결과가 없으면 NoResultException에러가 터지고
        * 결과가 둘 이상이면 NonUniqueResultException에러가 터진다.
    * 값이 정확히 하나가 있다고 보장될 때 사용한다.
    * 추가로 Spring Data JPA에서는 null이나 Exception을 반환시켜준다.

---

### 파라미터 바인딩

이런 식으로 사용한다.

* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> query = em.createQuery("select m from Member as m where m.username=:username", Member.class);
            query.setParameter("username", "member1");

            Member singleResult = query.getSingleResult();
            System.out.println(singleResult.getAge());

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

---

![](https://i.imgur.com/AwNy6Ie.png)

---

주로 이런 식으로 연결해서 사용해준다.

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            Member singleResult = em.createQuery("select m from Member as m where m.username=:username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            
            System.out.println(singleResult.getAge());

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

---

참고로 파라미터 바인딩은 위치 기반으로도 찾아올 수 있지만, 이는 쓰지 않는다.
-> 위치는 변경될 수도 있기 때문이다.

---

추가로 Spring Data JPA에서 파라미터 바인딩은 Collection도 가능하다. 예를 들어

```
@Query("select m from Member m where m.username in :names")
List<member> findByNames(@Param("names") List<String> names);
```

이런 식으로 가능하다.

---


## 프로젝션
* SELECT 절에 조회할 대상을 지정하는 것이다.
* 대상
    * Entity, 임베디드타입, 스칼라타입(숫자, 문자등 기본 데이터타입)
* SELECT m FROM Member m
    * 엔티티 프로젝션
* SELECT m.team FROM member m
    * 엔티티 프로젝션
* SELECT m.address FROM Member m
    * 임베디드 타입 프로젝션
* SELECT m.username, m.age FROM Member m
    * 스칼라 타입 프로젝션
* DISTINCT로 중복 제거 가능

여기서 중요한것은
엔티티 프로젝션 : 영속성 컨텍스트가 관리함
임베디드 타입 프로젝션 : 
스칼라 타입 프로젝션 : 

### 프로젝션 - 여러 값 조회
SELECT m.username, m.age FROM Member m
1. Query타입으로 조회
2. Object[]타입으로 조회
3. new 명령어로 조회

#### Query타입으로 조회
변환 타입이 명확하지 않을 때 사용하므로 여러 값 조회가 가능함.

* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            List memberList = em.createQuery("select m.username, m.age from Member as m").getResultList();

            Object o = memberList.get(0);
            Object[] result = (Object[]) o;

            System.out.println("username = " + result[0]);
            System.out.println("age = " + result[1]);

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

---

![](https://i.imgur.com/jlqeQU2.png)

---

#### Object 타입으로 조회
Typed를 사용하는데 여기서 Type을 Object로 조회한다.

이런 느낌이다.

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            List<Object[]> memberList = em.createQuery("select m.username, m.age from Member as m").getResultList();

            Object[] result = memberList.get(0);

            System.out.println("username = " + result[0]);
            System.out.println("age = " + result[1]);

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

---

#### new 명령어로 조회
* 단순값을 DTO로 바로 조회

* MemberDTO 생성

```
public class MemberDTO {
    private String username;
    private int age;

    public MemberDTO(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

---

* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member as m", MemberDTO.class).getResultList();

            MemberDTO memberDTO = result.get(0);
            System.out.println("memberDTO.username = " + memberDTO.getUsername());
            System.out.println("memberDTO.age = " + memberDTO.getAge());

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

JPQL의 내에서 new jpa.MemberDTO 이런 식으로
해당 위치를 가져와 생성자를 호출하듯 만들어낼수 있다.

MemberDTO에서 이미 생성자를 만들어 두었기 때문에 이렇게 하면 생성자를 사용해 바로 적용 가능하다.

---

![](https://i.imgur.com/p3vMpAs.png)

잘 나온다.

---

## 페이징
* JPA는 페이징을 두 개의 API를 사용해서 추상화한다.
* setFirstResult(int startPosition)
    * 조회 시작 위치(0부터 시작)
* setMaxResults(int maxResult)
    * 조회할 데이터 수

실제로 사용해 보면

---

* Member클래스

```
@Entity
public class Member {
    @Id @GeneratedValue
    private Long id;

    private String username;
    private int age;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
```

toString을 정의해준다.
여기서 Team은 빼주는데, 양쪽에서 계속 호출하면 무한루프에 빠질 수 있기 때문이다.


---

* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            for(int i=0; i<100; i++){
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                            .setFirstResult(1)
                            .setMaxResults(10)
                            .getResultList();

            System.out.println("size = " + result.size());
            for(Member member1 : result) System.out.println("member1 = " + member1);

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

order by ~ desc를 사용하여 나이가 많은 사람 순서대로 가져오게 된다.
여기서 setFirstResult(1)로 설정해 줬다. 시작이 0이니까 하나를 빼고 값을 가져올 것이다.

---

![](https://i.imgur.com/IpmdIA1.png)

이렇게 가져온다.
그리고 본래 페이징이 상당히 귀찮은데...이거를 바로 해준다는 장점이 있다.
예를 들어 Oracle DB를 쓴다면 

![](https://i.imgur.com/HjxJ5JA.png)

이런 느낌이다.

---

## 조인
* 내부 조인
    * SELECT m FROM Member m [INNER] JOIN m.team t
* 외부 조인
    * SELECT m FROM Member m LEFT [OUTER] JOIN m.team t
* 세타 조인
    * select count(m) from Member m, Team t where m.username = t.name

### 내부조인

* Member

```
@Entity
public class Member {
    @Id @GeneratedValue
    private Long id;

    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
```

---

* Team

```
@Entity
public class Team {
    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
```

---

* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            String query = "select m from Member m inner join m.team t";
            List<Member> result = em.createQuery(query, Member.class).getResultList();

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

위에서 Member을 LAZY로 해 주어야 한다.
그래야 Member을 찾을 때 바로 Team에 대한 검색을 하지 않는다.

---

![](https://i.imgur.com/uRyAww9.png)

잘 가져올 수 있다.

---

### 외부조인

* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            String query = "select m from Member m left outer join m.team t";
            List<Member> result = em.createQuery(query, Member.class).getResultList();

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

조건만 살짝 바꿔주면

---

![](https://i.imgur.com/JbCq7rt.png)

나온다.

---

### 세타 조인

* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            String query = "select m from Member m, Team t where m.username = t.name";
            List<Member> result = em.createQuery(query, Member.class).getResultList();

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

---

![](https://i.imgur.com/ffQIjJ1.png)

---

### Join - ON 절
* ON절을 활용한 조인(JPA 2.1부터 지원)
    * 조인 대상 필터링 가능
        * ex) 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인함
        * `SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'A'`
    * 연관관계 없는 엔티티 외부 조인(하이버네이트 5.1부터)
        * ex) 회원의 이름과 팀의 이름이 같은 대상 외부 조인
        * `SELECT m, t FROM Member m LEFT JOIN Team t on m.username = t.name`

---

## 서브쿼리
### 서브쿼리 지원함수
* [NOT] EXISTS (subquery) 서브쿼리에 결과가 존재하면 참
    * select m from Member m where exists(select t from m.team t where t.name = '팀A')
* ALL | ANY | SOME (subquery)
    * ALL 모두 만족하면 참
    * ANY, SOME : 조건을 하나라도 만족하면 참
        * select o from Order o where o.orderAmount > ALL (select p.stockAmount from Product p)
        * select m from Member m where m.team = ANY (select t from Team t)
* [NOT] IN (subquery) 서브쿼리 결과 중 하나라도 같은 것이 있으면 참

#### JPA서브쿼리의 한계
* JPA는 WHERE, HAVING절에서만 서브쿼리 사용 가능
* SELECT절도 가능(하이버네이트에서 지원해줌)
* **FROM절의 서브쿼리는 현재 JPQL에서 불가능**
    * 조인으로 풀 수 있으면 풀어서 해결한다. 

## JPQL 타입 표현과 기타식
문자, 숫자 이런걸 어떻게표현할거냐?

* 문자
    * ''
* 숫자
    * L(Long), D(Double), F(Float)
* Boolean
    * TRUE, FALSE
* ENUM
    * 자바의 전체 패키지명을 포함해서 넣는다.
* 엔티티 타입
    * TYPE(m) = Member(상속 관계에서 사용)
        * 상속받은곳의 DTYPE을 가진것 가져오도록 한다.
* EXISTS, IN
* AND, OR, NOT
* = > <= ...
* BETWEEN, LIKE, IS NULL 등등...

---

## 조건식(CASE식)

* 기본 CASE식
    * 조건
```
    select
        case when m.age <= 10 then '학생'
             when m.age >= 60 then '경로'
             else '일반'
         end
     from Member m
```

* 단순 CASE식
    * 매치
```
    select
        case t.name
            when '팀A' then '인센티브110%'
            when '팀B' then '인센티브120%'
            else '인센티브105%'
        end
    from Team t
```

* COALESCE
    * 하나씩 조회해서 null아니면 반환
    * 사용자 이름이 없으면 이름 없는 회원을 반환
        * select COALESCE(m.username, '이름없음') from Member m
* NULLIF
    * 두 값이 같으면 NULL, 다르면 첫번째 값 반환
    * 사용자 이름이 '관리자'면 null, 나머지는 본인의 이름을 반환
        * select NULLIF(m.username, '관리자') from Member m

---

## JPQL 기본 함수
* JPQL이 제공하는 표준 함수
    * CONCAT
        * 두 문자열을 합치는 함수
    * SUBSTRING
        * 문자열 자르기
    * TRIM
        * 공백 제거
    * LOWER, UPPER
        * 대소문자 변경
    * LENGTH
        * 문자 길이
    * LOCATE
        * 해당 문자의 문자 내 위치 찾기
    * ABS, SQRT, MOD
        * 수학 function들
    * SIZE(JPA용도)
        * 해당 collection의 크기 return
    * INDEX(JPA용도)
        * 일반적으로는 사용 X
            * @OrderColumn을 사용할 때에 쓴다.
                * Collection의 위치값을 구할 때 사용.
* 사용자 정의 함수
    * 하이버네이트는 사용전 방언에 추가해야 한다.
        * 새로 Class를 만들어서 진행하며, extends받는 Dialect내의 참조를 참고하여 진행한다.
            * 이후에 persist에 해당Dialect를 등록해주면 된다.
                * 코드를 통해 보자면

- MyH2Dialect 사용

![](https://i.imgur.com/SUqYqXG.png)

```
public class MyH2Dialect extends H2Dialect {
    public MyH2Dialect(){
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }
}
```

이곳에 있는 regusterFunction내 코드는 H2Dialect 내에 미리 정의되어 있으므로 사용할 함수를 쓰면 된다.

---

- persistence.xml 코드 변경

![](https://i.imgur.com/BqHovxZ.png)

사용해줄 dialect로 변경해 준다.

---

- JpaMain 변경

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Member member1 = new Member();
            member1.setUsername("member1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            em.persist(member2);

            em.flush();
            em.clear();

            String query = "select function('group_concat', m.username) From Member m ";
            List<String> result = em.createQuery(query, String.class).getResultList();

            for(String s : result) System.out.println("S = " + s);

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

---

![](https://i.imgur.com/hfJUWjK.png)

원하는 함수가 실행되었다.

---

## 경로 표현식
.(점)을 찍어 객체 그래프를 탐색하는 것

![](https://i.imgur.com/NRYYuj2.png)

* 상태 필드
    * 단순히 값을 저장하기 위한 필드이다.
* 연관 필드(연관관계를 위한 필드)
    * 단일 값 연관 필드
        * @ManyToOne, @OneToOne
            * 대상이 엔티티이다.
    * 컬렉션 값 연관 필드
        * @OneToMany, @ManyToMany
            * 대상이 컬렉션이다.

### 경로 표현식의 특징
* 상태 필드
    * 경로 탐색의 끝, 탐색X
        * 즉 여기 이상으로 갈 수 있는곳이 없다는것이다.
* 단일 값 연관 경로
    * 묵시적 내부조인(inner join)발생, 탐색O
        * 계속해서 .(점)을 찍어서 그 아래 값에 대한 탐색이 가능하다.
* 컬렉션 값 연관 경로
    * 묵시적 내부 조인 발생, 탐색X
        * 컬렉션으로 가져오게 되어서 더이상 내부의 값을 가져오는 것은 안된다.
            * 명시적인 join을 걸어준 후에 그 별칭을 통한 탐색은 가능하다.

단, 실무에서는 묵시적 조인을 사용하지 말고 명시적 조인을 사용하도록 한다.
그 이유는 join이 SQL튜닝의 중요 포인트인데, 묵시적 조인은 이 조인이 일어나는 상황을 한눈에 파악하기 어렵기 때문이다.

---

## 패치 조인(fetch join)
실무에서 매우 중요하다. 
* SQL의 조인 종류에 해당하지는 않는다.
* JPQL에서 성능 최적화를 위해 제공하는 기능이다.
* 연관된 엔티티나 컬렉션을 SQL한 번에 조회하는 기능.
* join fetch 명령어 사용

예를 들어 회원을 조회하면서 연관된 팀을 함께 조회하려 한다(SQL한번에)

select m from Member m join fetch m.team
-> SELECT M.*, T.* FROM MEMBER M INNER JOIN TEAM T ON M.TEAM_ID=T.ID

---

아래와 같이 inner join을 사용하여 Team이 있는 member들을 가져온다고 가정한다면
![](https://i.imgur.com/iy710a2.png)

* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query = "select m From Member m ";
            List<Member> result = em.createQuery(query, Member.class).getResultList();

            for(Member member : result) System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

먼저 이렇게 member를 가져온 후에 찾아내면
이전에 member에서 @ManyToOne을 LAZY로 설정해 두었기 때문에 team은 proxy로 호출된다.
따라서 지연 로딩이 일어나게 된다.

![](https://i.imgur.com/ZE9EXIR.png)

-> 처음 회원1에 대해 팀A가 SQL을 통해 가져와진다.
-> 다음 회원2에 대해 팀A는 1차캐시된다.
-> 다음 회원3에 대해 팀B는 SQL을 통해 가져와진다.

이렇게 되면 굉장히 비효율적인 쿼리가 이루어진다. (N+1)
모든 경우에 대해 따로따로 쿼리가 돌아가기 때문.

---

이를 해결하기 위해 query부분을 fetch join으로 변경해준다.

`String query = "select m From Member m join fetch m.team";`

이렇게 하면 조회할 때에 한꺼번에 값들을 가져온다.
참고로 이미 member에서 지연 로딩으로 설정해 주었지만 fetch가 우선된다.

![](https://i.imgur.com/rrmxIjK.png)
이런식으로
그리고 위에서 한꺼번에 값들을 가져왔기 때문에, 이는 프록시가 아닌 실제 데이터이다.

---

### 컬렉션 페치 조인
일대다 관계, 컬렉션 페치 조인

반대로 일대다에 관한 조인이다.

---

* JpaMain

```
select t from Team t join fetch t.members where t.name = '팀A'
-> SELECT T.*, M.* FROM TEAM T INNER JOIN MEMBER M ON T.ID=M.TEAM_ID WHERE T.NAME = '팀A'

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query = "select t From Team t join fetch t.members";
            List<Team> result = em.createQuery(query, Team.class).getResultList();

            for(Team team : result) System.out.println("team = " + team.getName() + ", " + team.getMembers().size());

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

이런 식으로 일대다에서 join을 걸어 확인하게 되면...

---

![](https://i.imgur.com/rKrYfgz.png)

이렇게 나온다. 그런데 보면
팀A에 대해 members가 2개 걸리는것이 총 2번 출력된다...

그 이유는 아래와 같다.

![](https://i.imgur.com/nYqKmHO.png)

이런 식으로 팀A는 회원1, 회원2를 각각 갖는다.
이를 join해서 가져오게 되면 각 회원 1, 2에 대해 팀A를 가져서 실제로는 2번 출력되게 되는 것이다.

이렇게 DB입장에서 일대다 join하면 결과가 뻥튀기된다.
이 값에 대해

1. JPA는 DB와 통신한 결과를 모두 가져온다.
2. 영속성 컨텍스트에서 팀A의 결과(회원1에 관한)를 저장한다.
3. 영속성 컨텍스트에 팀A가 저장되어 있는 상태에서, 또다른 결과(회원2에 관한)를 저장한다.
4. JPA는 DB에서 받은 결과의 수만큼 출력하기 때문에
5. 같은 주소값을 가진 결과가 2개 출력되게 된다.

### 페치 조인과 DISTINCT
기존 SQL의 DISTINCT만으로는 중복된 결과를 모두 제거하기 힘들다.

JPQL의 DISTINCT는 총 2가지 기능을 제공하는데
1. SQL에 DISTINCT를 추가
2. 애플리케이션에서 엔티티 중복 제거


`String query = "select distinct t From Team t join fetch t.members";`

이런 식으로 distinct를 적용해 주면

![](https://i.imgur.com/YoxT9tI.png)
1. SQL자체에 DISTINCT 적용

![](https://i.imgur.com/mUL68rR.png)
2. 엔티티 중복 제거

가 되었음을 확인할 수 있다.

참고로, 일대다는 DB입장에서는 어쨌든 데이터가 뻥튀기된다.
근데 다대일은 데이터가 뻥튀기되지는 않는다.
이를 알아둘것.


### 페치조인과 일반조인의 차이
일반 조인 실행시 연관된 엔티티를 함께 조회하지 않음.

예를 들어

`String query = "select t From Team t join t.members";`

이런 식으로 일반 join만을 실행하면


![](https://i.imgur.com/CUudUJB.png)

team의 값만 처음에 가져오고

![](https://i.imgur.com/YJ9DQJb.png)

이후에 요청받은 사항에 대해 추가로 select를 실행하는 것을 확인할 수 있다.

즉
-> 페치 조인을 사용할 때에만 연관된 엔티티도 함께 조회(즉시 조인)
-> 페치 조인은 객체 그래프를 SQL한번에 조회하는 개념이다. 

---

### 페치 조인의 특징과 한계
* 페치 조인 대상에는 별칭을 줄 수 없다.
    * 페치 조인은 자신과 관련된 대상을 다 가져오는 것이다. 이상하게 동작할 수 있다.
        * 객체 그래프는 기본적으로 관련된 대상을 모두조회하는 것이다. where같은 것을 써서 일부만 사용하는것은 지양해야한다.
            * 데이터의 정합성이 깨질 수 있기 때문.
* 둘 이상의 컬렉션은 페치 조인할 수 없다.
    * 일대다의 경우도 데이터 뻥튀기되는데 이경우는 일대다대다로 이루어진다.
        * 잘못하면 데이터가 예상치 못하게 매우 커져버릴 수 있다.
* 컬렉션을 페치 조인하면 페이징 API(setFirstResult, setMaxResults)를 사용할 수 없다.
    * 데이터가 뻥튀기 되었는데 그걸 잘라서 가져오게 되는것은 안되기 때문.
    * 일대일, 다대일같은 단일 값 연관 필드들은 페치조인해도 페이징 가능.
    * 하이버네이트는 경고 로고를 남기고 메모리에서 페이징(**매우위험**)
        * 일대다 -> 다대일로 변경해서 사용할 수 있다.
            * 다만 이경우 @BatchSize()를 사용하여 한꺼번에 N개에 해당하는 개수를 로딩해 버릴수 있다(N+1해결을 위해)
            * 혹은 persisten.xml에 hibernate.default_batch_fetch_size를 사용하여 global로 설정할 수 있다.
* 연관된 엔티티들을 SQL한 번으로 조회한다 -> 성능 최적화
* 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선된다.
    * @OneToMAny(fetch=FetchType.LAZY) // 글로벌 로딩전략
* 실무에서 글로벌 로딩 전략은 모두 지연로딩이다.
* 최적화가 필요한 곳은 페치 조인 적용
* 모든것을 페치조인으로 해결할수는 없다.
* 페치 조인은 객체그래프를 유지할 때 사용하면 효과적이다.
    * 현재 위치에서 어딘가의 위치로 찾아갈 때
        * 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야하면, 페치조인보다는 일반조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는것이 효과적이다.

---

## 한계 돌파

이전에 컬렉션을 페치조인하면 일대다 조인이 발생하여 페이징을 할 수 없다고 했다.
일을 기준으로 페이징 하고싶은데, DB에서 데이터는 다를 기준으로 row가 생성되기 때문이다.

그러면 페이징 + 컬렉션 엔티티를 함께 조회하려면 어떻게 해야할까??

페이징+컬렉션 엔티티 조회를 코드도 단순하고 성능 최적화도 보장하는 방법이 있다.

1. xToOne관계를 모두 페치조인한다. 이 xToOne관계들은 딱히 데이터 row수 증가에 영향을 끼치지 않기 때문이다.
2. 컬렉션은 지연 로딩으로 조회한다.
3. 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size, @BatchSize를 적용한다.

![](https://i.imgur.com/4Pr3egg.png)
![](https://i.imgur.com/hRiLGBT.png)


```
select orderitems0_.order_id as order_id5_5_1_, orderitems0_.order_item_id as order_it1_5_1_, orderitems0_.order_item_id as order_it1_5_0_, orderitems0_.count as count2_5_0_, orderitems0_.item_id as item_id4_5_0_, orderitems0_.order_id as order_id5_5_0_, orderitems0_.order_price as order_pr3_5_0_ from order_item orderitems0_ where orderitems0_.order_id in (?, ?)
select orderitems0_.order_id as order_id5_5_1_, orderitems0_.order_item_id as order_it1_5_1_, orderitems0_.order_item_id as order_it1_5_0_, orderitems0_.count as count2_5_0_, orderitems0_.item_id as item_id4_5_0_, orderitems0_.order_id as order_id5_5_0_, orderitems0_.order_price as order_pr3_5_0_ from order_item orderitems0_ where orderitems0_.order_id in (4, 11);
```


```
select item0_.item_id as item_id2_3_0_, item0_.name as name3_3_0_, item0_.price as price4_3_0_, item0_.stock_quantity as stock_qu5_3_0_, item0_.artist as artist6_3_0_, item0_.etc as etc7_3_0_, item0_.author as author8_3_0_, item0_.isbn as isbn9_3_0_, item0_.actor as actor10_3_0_, item0_.director as directo11_3_0_, item0_.dtype as dtype1_3_0_ from item item0_ where item0_.item_id in (?, ?, ?, ?)
select item0_.item_id as item_id2_3_0_, item0_.name as name3_3_0_, item0_.price as price4_3_0_, item0_.stock_quantity as stock_qu5_3_0_, item0_.artist as artist6_3_0_, item0_.etc as etc7_3_0_, item0_.author as author8_3_0_, item0_.isbn as isbn9_3_0_, item0_.actor as actor10_3_0_, item0_.director as directo11_3_0_, item0_.dtype as dtype1_3_0_ from item item0_ where item0_.item_id in (2, 3, 9, 10);
```



---

이렇게 하면

1. 처음에 xToOne으로 fetch join으로 만들어진 데이터들을 가져온다.
2. hibernate.default_batch_fetch_size 에 설정한 데이터만큼 추가로 in 쿼리를 사용하여 데이터를 더 뽑아온다.
3. 그리고 그 Many쪽과 join되어 있는 데이터를 또 in을 사용해서 가져와준다..

이를 사용하면

**최적화도 엄청 잘되고(size를 100으로 설정하면 1000개를 가져온다 해도 10번의 루프밖에 돌지 않는다.), 페이징도 처음에 fetch join으로 가져온 것을 기반으로 진행하여서 가능하다!!!**

---

추가로 해당 방식은 기존 방식에 비해 정규화된 데이터를 제공한다.

예를 들어 기존처럼 모든 데이터를 싹다 fetch하는 경우는 쿼리 자체는 한번에 끝나지만, 데이터 자체는 많은 중복이 발생하게 된다.
이는 DB -> 어플리케이션으로의 통신이 진행될 때에 전송되는 데이터가 많다는 것을 뜻하며 용량 이슈로 이어지게 된다.

반대로 만약 처음에 xToOne만을 fetch하고, 이후 해당 방식으로 진행하게 된다면
1. 처음에 fetch한 데이터는 정확하게 원하는 데이터만을 중복 없이 가진다.
2. in으로 추가로 가져온 데이터는 중복이 없다.
3. 만약 in으로 가져온 데이터와 또 연결되어 있는 데이터가 존재하는 경우, 이곳에서도 중복 없이 데이터를 가져올 수 있다.

즉 해당 방식을 사용하는 경우는 정규화된 상태로 DB조회를 진행할 수 있다는 장점 또한 가진다.

---

추가로 하나씩 적용하고 싶으면 엔티티에 @BatchSize 를 쓰면 된다.

### 장점
* 쿼리 호출 수가 1+N에서 1+1로 최적화된다!!
* 조인보다 DB데이터 전송량이 최적화 된다.(하나씩 싹다 조회하기 때문에 중복이 없다)
    * 기존 페치 조인 방식과 비교하면 쿼리 호출 수는 증가하지만 DB데이터 전송량은 감소한다.
* 페이징이 가능해진다!!

### 결론
xToOne관계는 페치조인하고, LAZY로 선언해서 바로 가져오지 않도록 하고 hibernate.default_batch_fetch_size로 최적화하자.

참고로 default_batch_fetch_size는 100 ~ 1000사이를 선택한다.
1000넘어가면 오류를 일으키는 DB가 가끔 있기도 하고 DB에 부하가 걸릴수도 있다.

부하가 없는 선에서 늘려가면서 하면 될듯싶다.

---




## JPQL 다형성 쿼리

![](https://i.imgur.com/hewXxFk.png)
여기서 조회 대상을 특정 자식으로 한정

---

### TYPE
예를 들어 Book, Movie만을 찾으려 한다면
[JPQL]
`select i from Item i where type(i) IN (Book, Movie)`

[SQL]
`select i from i where i.DTYPE in ('B', 'M')`

---

### TREAT
부모인 item과 자식 Book이 있다.

[JPQL]
`select i from Item i where treat(i as Book).auther = 'kim'`

[SQL]
`select i.* from Item i wher i.DTYPE = 'B' and i.auther = 'kim'`

---

## 엔티티 직접 사용
JPQL에서 엔티티를 직접 사용하면 SQL에서 해당 엔티티의 기본 키 값을 사용한다.

[JQPL]
`select count(m.id) from Member m //엔티티 아이디를 사용`
`select (m) from Member m // 엔티티 직접 사용`

[SQL] 둘 다 같은 SQL실행됨.
`select count (m.id) as cnt from Member m`

> 그러니까 Entity를 구분하는것 자체가 PK이기 때문에 Entity를 바로 넘기면 PK를 통해 조작한다.

즉 엔티티를 파라미터로 전달하거나

* JpaMain

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query = "select m From Member m where m = :member";
            Member findMember = em.createQuery(query, Member.class).setParameter("member", member1).getSingleResult();

            System.out.println("findMember = " + findMember);

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

![](https://i.imgur.com/wgyt0GE.png)

---

식별자를 직접 전달하나

```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query = "select m From Member m where m.id = :memberId";
            Member findMember = em.createQuery(query, Member.class).setParameter("memberId", member1.getId()).getSingleResult();

            System.out.println("findMember = " + findMember);

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

![](https://i.imgur.com/n9cpacB.png)

똑같은 SQL이 실행됨을 확인 가능하다.

---

당연히 외래키 값을 사용해도 똑같이 사용된다.

---

## Named쿼리 - 어노테이션
@NamedQuery 라는 어노테이션을 사용해서 미리 쿼리를 정의해둘 수 있다.
즉
* 미리 정의해서 이름을 부여해두고 사용하는 JPQL
* 정적 쿼리
* 어노테이션, XML에 정의
* 애플리케이션 로딩 시점에 초기화 후 재사용
    * 정적 쿼리이기 때문에 변할 염려가 없어 로딩 시점에 바로 파싱하여 캐시시킨다.
* 애플리케이션 로딩 시점에 쿼리를 검증
    * 컴파일 시점에 바로 에러의 검증이 가능하다.
    * SQL을 바로바로 확인할 수 있다는 큰 장점이 있다.

---

* Member에서 정의하기

```
@Entity
@NamedQuery(
        name = "Member.findByUsername"
        , query = "select m from Member m where m.username = :username"
)
public class Member {
    @Id @GeneratedValue
    private Long id;

    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
```

---

* JpaMain
```

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                            .setParameter("username", "회원1")
                            .getResultList();

            for(Member member : resultList) System.out.println("member = " + member);

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

---

![](https://i.imgur.com/pApg1tC.png)

이렇게 원하는 결과가 바로 실행된다.
또, 미리 정적으로 선언해둔 쿼리는 컴파일 단계에서 에러를 잡아준다.

---

* Named쿼리 설정은 XML이 항상 우선권을 가진다.
* 애플리케이션 운영 환경에 따라 다른 XML을 배포할 수 있다.

추가로 Spring Data JPA에서 중요한 것이
**Repository(DAO)에서 @Query() 로 정의하는 쿼리가 바로 이 Named쿼리**이다.
따라서 @Query 어노테이션을 사용해서 정의한 쿼리는 문법 오류 발생시 바로 잡을 수 있다.
-> ~~이걸 부를때는 NoName NamedQuery라고 한다고 함.~~

---

## JPQL 벌크 연산
뭉탱이로 Update / Delete 등을 하는 것.

예) 재고가 10개 미만인 모든 상품의 가격을 10% 상승한다.

본래 JPA변경 감지 기능으로 실행하려면 너무 많은 SQL이 실행된다.
1. 재고가 10개 미만인 상품을 리스트로 조회한다.
2. 상품 엔티티의 가격을 10% 증가한다.
3. 트랜잭션 커밋 시점에 변경감지가 동작한다.

이렇게 JPA변경 감지 기능으로 하면 100건에 대해 100개의 Update가 실행된다.
JPA는 보통 벌크보다는 실시간(단건)의 연산에 더 치중되어있기 때문이다.

이걸 한꺼번에 해주는것이 벌크연산이다.

---

예를들어 전체 회원의 나이를 20살로 맞춘다고 한다면


```
public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 하나의 Transaction 내에서 코드를 실행시키고, 문제가 없으면 commit까지 해 준다.
        try{
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            int resultCount = em.createQuery("update Member m set m.age = 20").executeUpdate();

            System.out.println(resultCount);

            tx.commit();
        } catch (Exception e){  // 문제가 발생하면 Transaction rollback 진행
            tx.rollback();
        } finally {  // 로직이 끝나면 무조건 em을 닫아준다.
            em.close();
        }
        // application끝날 때에는 emf를 닫아 준다.
        emf.close();
    }
}
```

![](https://i.imgur.com/T39SkIv.png)

이렇게, 한꺼번에 되고 변경된 row를 받아올 수 있다.

---

따라서 벌크연산은
* 쿼리 한번으로 여러 테이블 로우 변경(엔티티)
* executeUpdate()의 결과는 영향받은 엔티티의 수를 반환한다.
* UPDATE, DELETE 지원
* 벌크 연산 사용시 자동으로 em.flush가 호출된다.
    * 저것은 DB에만 반영되기 때문에 이전에 저장한 값들은 변화되지 않고 남아있는다!

### 벌크 연산 주의점
* 벌크 연산은 영속성 컨텍스트를 무시하고 DB에 직접 쿼리한다. 여기서 데이터가 꼬이지 않게 하려면
    * 벌크 연산을 먼저 실행
    * Or 벌크 연산 수행 후 영속성 컨텍스트 초기화
        * 벌크 연산이 수행되면 flush가 한번 이루어진다. 따라서 이후 영속성 컨텍스트를 초기화하고 진행해주면 된다.(즉 캐시를 지워 다시 처음부터 찾기)

---
## 변경 감지와 병합(merge)

> 정말정말정말 중요한 내용이니 꼭 완벽하게 이해해야 한다!!

### 준영속 엔티티
영속성 컨텍스가 더이상 관리하지 않는 엔티티를 말한다.
ItemController의 updateItem처럼
새로운 객체를 생성하였지만, ID를 기존의 값을 가져와서 저장한 객체(Id가 세팅된 객체) -> 즉 JPA에 한번 들어갔다 나온 객체를 준영속 상태의 객체라 한다.

Book객체는 이미 DB에 한번 저장되어 식별자가 존재한다.
이렇게 임의로 만들어 낸 엔티티도 기존 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있다.

본래 JPA과 관리해주는 영속상태 엔티티와 달리 준영속 엔티티는 따로 관리의 대상이 아니다. 즉, 변경을 해도 따로 업데이트를 해주지는 않는다는 것이다.

그렇다면 이런 준영속상태 엔티티를 어떻게 수정해 줄 수 있을까?

* 변경 감지 기능 사용
* 병합(merge) 사용

### 변경 감지 기능 사용(Dirty Checking)

```
@Transactional
public void updateItem(Long itemId, Book bookParam){
    Item findItem = itemRepository.findOne(itemId);
    findItem.setPrice(bookParam.getPrice());
    findItem.setName(bookParam.getName());
    findItem.setStockQuantity(bookParam.getStockQuantity());
    // ... 이렇게 필드를 채워서 진행해주면 findOne을 통해 영속상태로 가져와준 값이 param으로 세팅되게 된다.
    // 그러면 따로 업데이트를 해주지 않아도 영속성 컨텍스트의 관리대상이 되어서 Dirty Checking을 해준다.
    // 그래서 이렇게 하면 바로 업데이트 한다!!!
}
```

간단히 말해서 내가 변경할 값들이 세팅되면, DB에서 해당 ID를 통해 검색을 진행해 주고(이 때 바로 영속화된 findItem이 존재한다.) 이 findItem의 필드를 변경할 값들로 채워주면 영속성 컨텍스트가 바로 DirtyChecking해주어 변경을 감지한다. 그리고 변경된 값이 있으면 Transactional 내에서 알아서 업데이트 시켜준다!!!

### 병합 사용(Merge)
이는 준영속 상태를 영속으로 바꾸어주는 것이다.

`em.merge();`

이렇게 merge를 써주면 위의 코드를 JPA가 짜줘서 동작하게 된다.
다만 둘 간의 차이가 있다.

![](https://i.imgur.com/oGLfT2v.png)

merge의 동작방식은
1. 먼저 1차캐시 엔티티를 찾고, 없으면 DB에서 엔티티를 가져온다.
2. 가져온 엔티티에 준영속상태인 값들을 하나씩 채워넣어준다.
3. 그렇게 채워넣어준 엔티티를 return한다.

이렇게 위의 코드에서 return Item 같은 식으로, 영속화된 엔티티를 return시켜주는 것이다.

그러니까 즉, **이 방식은 준영속화 되어있는 엔티티를 영속화로 바꾸어 주는것이 아니라, merge를 통해 영속화 되어있는 새로운 엔티티를 반환**시켜 주는 것이다!

#### 병합의 주의점
변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만, 병합을 사용하면 모든 속성이 변경된다.
그래서 이제 병합할 때에 값이 없으면 'null'로 업데이트 될 위험성이 있다.
즉 병합은 모든 필드를 변경한다는 것이고, 이는 매우 위험하다!!

### 결론
그냥 병합은 쓰지 말고 Dirty Checking을 쓰자.

---

## API 뽑아내기

### 엔티티 조회
1. 엔티티를 조회해서 그대로 반환 -> 사용하면 안됨
2. 엔티티 조회 후 DTO로 변환
3. 페치 조인으로 쿼리 수 최적화 -> (2)에서 성능이 나오지 않을때 추가해주면 된다.
    * 컬렉션에서 페치 조인을 사용하는 경우 페이징을 사용하지 못하는 경우가 생긴다.
        * 그럴 때에는 이전에 배운 한계돌파를 사용한다.

### DTO 직접 조회
1. JPA에서 DTO를 직접 조회
2. 컬렉션 조회 최적화
    * 일대다 관계인 컬렉션은 IN절을 활용해서 메모리에 미리 조회해서 최적화한다.
        * 먼저 일에 해당하는 애들을 싹다 가져오고, 이후 다에 해당하는 부분을 IN절에서 로딩해서 가져온다.
3. 플랫 데이터 최적화
    * JOIN결과를 그대로 모두 가져와서 애플리케이션에서 원하는 모양으로 변환한다.
        * 중복 데이터 전체 들고온 후 애플리케이션에서 최적화 진행.

처음 방식은 코드가 단순하고 유지보수가 단순하다.
그리고 특정 주문 한건만 조회하면 이 방식으로도 성능이 잘 나온다.

컬렉션 조회 최적화는 코드가 복잡하다.
위의 방식에서의 문제점은 N+1을 1+1로 해결 가능하다.

플랫데이터 위와는 전혀 다른 접근방식이다.
한번에 모든 데이터를 패치해서 뻥튀기된 데이터를 들고와서 어플리케이션에서 이를 최적화한다.
이는 보통 위의 방식보다 성능이 좋지만, 어플리케이션에 부하가 생길 수 있으며 중복 데이터가 들어는 등의 단점을 가진다.
다만, 실무에서는 이정도로 최적화가 필요한 데이터는 최소 수백, 수천건이 필요한데 페이징이 불가능하다면...조금 선택하기 어려운 방법이라 한다.
또 위의 컬렉션 조회와 비교할 때에도 데이터의 전송이 많기 때문에 중복 전송이 증가하여 성능이 막 많이 증가하는 부분도 없다고 한다.

### 권장 순서
1. 엔티티 조회 방식으로 우선 접근한다.
    1-1. 페치조인으로 쿼리수 최적화하기.
    1-2. 컬렉션 최적화(페이징 유무에 따라)
2. 엔티티 조회 방식으로 해결이 안되면 DTO조회 방식 사용하기
3. 이 DTO방식으로도 안되면 NativeSQL이나 Spring JdbcTemplate사용하기

엔티티 조회 방식은 페치 조인이나, hibernate.default_batch_fetch_size를 바로 사용할수 있다.
이런 것들을 사용하면, 코드의 수정을 최소화한 상태로 옵션만 약간 변경해서 다양한 성능 최적화를 시도할 수 있다.

반면에 DTO를 직접 조회하게 되면 성능 최적화를 진행하거나, 성능 최적화 방법을 변경할 때에 많은 코드 변경을 요구한다.

참고로...DTO로 변경해서 뽑아오는 방식은 잘 사용하지는 않는다. 진짜 사람이 엄청나게 많지 않은경우에는 안쓰기 때문이다.
그리고 DTO를 사용하면 캐싱을 사용해서 적용한다. 이렇게 하면 성능 향상을 더 해줄 수 있기 때문이다. **엔티티의 경우는 캐시 사용을 해서는 안된다**!!

---

## OSIV와 성능 최적화
Open Session In View : 하이버네이트
Open EntityManager In View : JPA (관례상 OSIV라 한다.)

### OSIV ON

![](https://i.imgur.com/pt3FC7v.png)

Spring boot 어플리케이션을 처음 실행하면 다음과 같은 warn문구가 출력된다.
`2022-03-15 11:23:35.504  WARN 11052 --- [  restartedMain] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning`

이 기본값이 시작 시점에 warn로그를 남기는 것에는 이유가 있다.

JPA가 영속성 컨텍스트와 데이터베이스 커넥션을 처음 가져오는 시점은 기본적으로 DB transaction을 시작할 때이다.

* 데이터베이스 커넥션을 돌려주는 시점은
    * OSIV가 켜져 있으면
        * 해당 Transaction이 끝나고, 밖으로 나가도 반환하지 않는다.
            * 반환 시점은
                * API가 유저에게 반환될 때 까지 유지된다.
                * View가 렌더링이 끝날 때 까지 유지된다.

이 덕분에 영속성 컨텍스트가 Controller등에서도 살아 있어서 지연 로딩 등이 가능했다.

다만 해당 전략은 너무 오랫동안 DB커넥션 리소스를 사용한다.
따라서 실시간 트래픽이 중요한 애플리케이션에서는 커넥션이 말라버릴수가 있다. 이는 장애로 이어진다.

예를 들어서 컨트롤러에서 외부 API를 호출할 때 3초정도 걸린다 하면 그 3초동안 데이터베이스 커넥션을 사용중인 것이다.
그 시간동안 커넥션 리소스를 반환하지 못하고 유지해야 한다.

### OSIV OFF
![](https://i.imgur.com/lJGcEdP.png)

* spring.jpa.open-in-view: false
를 써서 off할 수 있다.

off하면 트랜잭션이 종료될 때에 영속성 컨텍스트를 닫고, 데이터베이스 커넥션도 반환한다.

이를 통해 커넥션 리소스를 낭비하지 않게 한다.
다만, 이렇게 되면 모든 지연로딩을 트랜잭션 안에서 처리해야 한다.
그리고 view template에서 지연로딩이 동작하지 않는다.
결론적으로 트랜잭션이 동작중인 동안 지연 로딩을 강제로 호출해 주어야 한다.

이 문제를 해결하는 방법이 있다.

#### 커멘드와 쿼리 분리
보통 비즈니스 로직은 특정 엔티티 몇개를 등록하거나 수정하기 때문에 성능에 크게 문제가 없다.
그런데 복잡한 화면 출력용 쿼리 등은 화면에 맞추어 성능을 최적화 하는 것이 중요하다.
그런데 복잡성에 비해 핵심 비즈니스에 큰 영향을 주는 것은 아니다.

화면 찍는것과 핵심 비즈니스의 관심사를 명확하게 분리하여 진행한다.

##### Query용 서비스 생성
관심사 분리 방법 중 하나는 로직에 따라 새로운 서비스를 만들어서 처리하는 것이다.

@Transactional 어노테이션을 받는 추가적인 서비스를 생성한다.

이후에 기존에 Controller등에서 처리하던(OSIV OFF시 영속성 컨텍스트의 영향에서 벗어나는) 영속성 컨텍스트 관련 로직을 여기로 옮겨서 처리한다.

---

### 결론

OSIV를 켜면 쿼리 서비스를 view나 controller에서 처리해 줄 수 있다.

그래서 실무에서 고객 서비스의 실시간 API는 OSIV를 끄고, ADMIN처럼 커넥션을 많이 사용하지 않는 곳에서는 OSIV를 켠다고 한다.

---
