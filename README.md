# BP-of-EA 
![ci](https://github.com/winter4666/BP-of-EA/actions/workflows/ci.yml/badge.svg)
[![codecov](https://codecov.io/gh/winter4666/BP-of-EA/branch/main/graph/badge.svg?token=SNV75ZZGZ5)](https://codecov.io/gh/winter4666/BP-of-EA)

本项目主要由本文档和demo构成。Demo主要用来展示文档中描述的我认为的一些比较好的企业级应用开发实践，当然也可以作为一个脚手架，用于从0到1快速搭建起一个生产级别可用的项目。
之所以命名better practice，而不是best practice，一方面是因为本人对技术存有的敬畏之心，另一方面也是希望本项目能一直与时俱进，不断的保持更新，不断寻求那个better practice。

## 业务介绍
本项目只是一个demo，所以我在这里虚构了一个所有人都能快速理解的业务需求：学生选课系统。

整体业务非常简单，一句话描述就是：老师创建课程，学生看到老师创建的课程，然后可以选择该课程加入到自己的课程表。

## 快速开始
进入本项目的[DockerHub页面](https://hub.docker.com/repository/docker/winter4666/bp-of-ea/general)，查看README。

## 核心理念
本项目的所有技术选择都将以下面这些原则作为指导方针：
* [KISS原则](https://en.wikipedia.org/wiki/KISS_principle)
* 需求优先，而非技术
* 标准化
* 面向对象
* [若无必要，勿增实体](https://en.wikipedia.org/wiki/Occam%27s_razor)
* [约定优于配置](https://en.wikipedia.org/wiki/Convention_over_configuration)

## Better Practice

### 架构
本项目使用了[六边形架构](https://alistair.cockburn.us/hexagonal-architecture/)。

在我看来，六边形架构并非一个足够形象的好名字，我更喜欢将它称之为同心圆架构，它将系统分为三个部分：处于圆心的程序核心逻辑部分，处于圆环的Adapter部分，以及处于圆外面的各种程序外部依赖（譬如数据库，消息队列，web服务等等）。

我们经常使用的[MVC Pattern](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller#cite_note-1)和[DAO Pattern](https://www.oracle.com/java/technologies/dataaccessobject.html)可看作六边形架构在程序入口部分和数据访问部分的实现，Controller类和Dao类都可看作系统的Adapter。

浏览本项目的项目结构，首先工程按照业务被划分成几个不一样的module，每一个module是一个文件夹。然后进入任一个module，我们总能看到一个文件夹叫domain，该文件夹里面存放的代码是系统的核心逻辑，也就是六边形架构的圆心部分，而除了domain之外的其它文件夹譬如controller，dao或message，其中的代码都属系统的adapter部分。

在我看来，使用六边形架构可以极大提高系统的可扩展性和可测试性。而且我甚至认为，六边形架构的思想，即把系统不可替换的核心逻辑与可替换的支撑性代码隔离开来，也可用于指导其它类型程序的开发，而不仅仅是企业级应用。

### 测试金字塔
基于六边形架构，本项目搭建起了一个[测试金字塔](https://insights.thoughtworks.cn/practical-test-pyramid/)。

金字塔主要由三种测试构成：对于程序的核心逻辑，也就是项目中处于domain文件夹下的代码，我使用了[单元测试](https://martinfowler.com/bliki/UnitTest.html)进行测试；对于程序的Adapter部分，我主要使用了[集成测试](https://martinfowler.com/bliki/IntegrationTest.html)进行测试；同时还会有少量的[端到端测试](https://martinfowler.com/bliki/BroadStackTest.html)对系统最重要的功能进行覆盖。

通过本项目的实践可以发现，如果系统严格遵循了六边形架构进行编程，那么编写测试不止变得更加简单清晰了，而且要达成几乎100%的测试覆盖率也并非不可能。

和大多数人理解的不一样，我认为单元测试和集成测试是处于金字塔的同一层级，而非上下层，而端到端测试则在它们之上，编写测试代码，大体原则如下：
* 单个**单元测试**和**集成测试**的测试范围应该尽可能小，尽可能达到当某个测试出错时，可以马上定位到可能有问题的代码位置。
* 单个**端到端测试**的测试范围应该尽可能的大，尽可能地模拟真实环境，保证系统关键功能总是正常可用的。
* **单元测试**和**集成测试**的数量应该很多，多到足以100%覆盖项目的所有代码。
* **端到端测试**的数量应该尽可能少，如果某部分功能可以由下层的**单元测试**和**集成测试**所测试，那么可以将其移到下层。

虽然编写测试代码会消耗大量时间，但是我认为在时间允许的情况下，足量的测试代码很有必要，它可以让我们重构代码时更自信，也能显著提升我们维护代码时的效率，同时测试代码是否好编写，实际上也是程序质量是否足够好的一个重要信号。

有不少人推崇[TDD](https://en.wikipedia.org/wiki/Test-driven_development)，这当然是一个非常理想化的编程方式，可是现实项目里，TDD的系列流程经常会让人觉得是自缚手脚，很难落地。我更实际一点，推崇**TPD**，也就是Test-protected development。

### MyBatis还是JPA?
MyBatis还是JPA？我认为这个问题几乎可以等同于是要面向过程还是面向对象。一般而言程序与关系数据库交互的业务逻辑部分大致会写成以下三种样子：
1. MyBatis + Service（大量业务逻辑，类似[Transaction Script](https://martinfowler.com/eaaCatalog/transactionScript.html)） + 贫血模型（仅是数据容器，不包含业务逻辑）
2. JPA + Service（仅含非常少量业务逻辑） + 充血模型（包含大量业务逻辑）
3. JPA + Service（大量业务逻辑，类似[Transaction Script](https://martinfowler.com/eaaCatalog/transactionScript.html)） + 贫血模型（仅是数据容器，不包含业务逻辑）

第一种方式似乎一直为各大互联网公司所青睐，毫无疑问，这种方式被大量使用是有其客观原因的。
一般而言，在这种情况下，程序可以对数据库操作进行更精细的控制，从而拥有更佳的性能和灵活性，同时面对复杂查询时，直接使用sql也非常方便。
但是缺点是，使用这种方式会逼迫我们对系统最核心的业务逻辑部分代码进行面向过程编程，我们完全放弃了面向对象带来的诸多好处。

第二种方式其实很少为人所采用，不过它却被Rod Johnson（Spring作者）和Eric Evans（DDD作者）所推崇，它最大的劣势是，在对抗关系模型和对象模型的阻抗不匹配的过程之中，程序会不可避免的损失一些性能。
举个例子，同样是更新数据库中的一条记录，使用第一种方式，一条update语句即可，可使用第二种方式，我们总是需要将该记录先取出来变成对象更新它，然后再将这个更新同步到数据库，不可避免的，系统增加了一次对数据库的IO访问。
但是同时，它拥有的最大优势就是，采取这种方式能够让我们进行面向对象编程。

我不大推荐使用第三种方式，在我看来，使用这种编程模式，系统既享受不到面向对象编程的好处，同时还要承担ORM框架会带来的复杂性，得不偿失。

对于前面两种。我认为若是系统的主要功能侧重在数据分析，或者对性能非常非常敏感，那么采用第一种方式是很合适的，事实上在这种情况下，我们甚至可以考虑不使用面向对象编程语言作为系统主要语言，因为反正也无法进行面向对象编程；
而若是系统的主要功能侧重在事务处理，那么我更推荐采用第二种方式。

本人是个面向对象的推崇者，自然而然，本项目采用了第二种方式，结合Hibernate的懒加载，二级缓存等功能，我觉得这种方式带来的性能损失是可接受的。希望本项目能成为一个示例，让大家看到使用面向对象编程之后，程序可读性、可扩展性的巨大提升。

### 验证逻辑应该放在哪里？
这也是个很有争议的问题，有很多人会习惯把验证逻辑直接放在Controller部分，但是我觉得，验证逻辑显然是业务逻辑，应该放在domain部分。

最明显的，程序的入口是多种多样的，有可能从http请求过来，也有可能从消息队列的消息过来，但是不管入口是从哪里过来，需要的验证逻辑还是不可缺少的。而如果我们把验证逻辑放在Controller层，那意味着当我们程序入口不再是http请求时，这段验证逻辑也会消失，这不是我们想要的。

下一个问题是应该放在domain部分的哪个地方，我推荐，如果可以的话，验证逻辑应该尽量放在业务对象被构造出来之前，同时我们应该控制业务对象空构造方法的访问权限，最终达到的效果是只要是通过正常途径（而非反射）构造的存在于系统中的对象，都肯定满足了某种约束条件。这样做可以大大提升程序的健壮性。具体代码实现可参考本demo，主要需用到建造器模式。

### 是否使用RESTful API？
本项目使用了RESTful API，因为我觉得它已经成为了一个事实上的标准，使用这种标准化的方式可以享受标准化带来的各种好处。本项目实现RESTful API时，参考了[GitHub REST API documentation](https://docs.github.com/en/rest)。

但是，我必须要说，RESTful API并不适用于所有项目。视需求而定，其它风格的API也是可以接受的，譬如[JSON-RPC](https://www.jsonrpc.org/)。但是最关键的问题是，在一个项目里，API的风格应是统一的。

### Gradle还是Maven？
Gradle擅长构建，而Maven擅长依赖管理。本项目选择了Maven，因为它足够简单，而且也完全可以满足我的需求。若是你的项目存在非常复杂的构建过程，或对构建速度也很苛刻的要求，可选择Gradle。