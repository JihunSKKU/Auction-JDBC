# 0. 실행 방법
본인의 postgreSQL ID와 pw를 makefile run에 넣습니다. 이후 아래 코드를 입력합니다.
```shell
make
make run
```

# 1. 프로젝트 목적

이 프로젝트에서는 온라인 경매 사이트를 위한 데이터베이스 시스템을 설계, 구현, 문서화 및 테스트하는 요구사항을 분석하는 것이 주요 목표입니다. 일반적으로 웹 사이트 구축은 디자인 측면의 결정을 내리고 HTML, CSS, JSP, JavaScript 등을 사용하여 사용자 인터페이스를 구현하는 프론트엔드 개발 팀이 참여합니다. 그러나 이 프로젝트에서는 백엔드 개발에 중점을 두고 있습니다.

프론트엔드 개발 팀이 아직 고용되지 않았기 때문에 회사는 그래픽 사용자 인터페이스 없이 텍스트 기반의 메뉴 인터페이스를 제공했습니다. 여러분은 이 프로젝트에서 백엔드 개발자로 참여하게 됩니다.

불행하게도, 회사는 아직 프론트엔드 개발자를 고용하지 않았기 때문에 회사는 그래픽 사용자 인터페이스 없이 텍스트 기반의 메뉴 기반 인터페이스를 제공했습니다. 다음은 제공된 기본 프로그램의 출력입니다.

참고: 기본 코드는 In-Ui-Ye-Ji 클러스터의 /home/swe3003/auction/ 디렉토리에 있습니다. 사용자가 선택한 항목에 해당하는 함수가 호출됩니다. 예를 들어, 사용자가 '1'을 선택하면 프로그램은 LoginMenu() 함수를 호출하며, 이 함수에서는 ID와 비밀번호를 요청합니다. ID와 비밀번호가 확인된 후, 프로그램은 다음과 같은 메뉴를 출력합니다. 사용자가 '3'을 선택하면 프로그램은 해당 함수를 호출하고 다음 레벨의 메뉴를 출력합니다. 백엔드 웹 애플리케이션 개발에 관해서는 Java가 가장 인기 있는 프로그래밍 언어 중 하나입니다. 그러므로 이 프로젝트를 구현하기 위해 Java와 JDBC를 사용하는 것을 권장합니다. 그러나 ODBC를 사용하길 선호한다면 그렇게 할 수 있습니다. 다만, 스켈레톤 코드의 도움 없이 처음부터 프로그램을 작성해야 합니다. 다양한 편리한 데이터베이스 애플리케이션 개발 프레임워크(View.js, Django, Ruby-on-Rails 등)가 있지만, 이 프로젝트에서는 사용을 삼가해 주십시오. 이는 입문 과정이며, 보다 고급 도구와 프레임워크에 뛰어들기 전에 기본 지식을 확실히 이해하는 것이 중요합니다.

# 2. 요구사항

## 2.1 데이터 및 쿼리

이 경매 시스템은 모든 사용자가 물건을 판매하고 둘러보고 구매할 수 있도록 허용합니다. 이 기본 기능을 지원하기 위해 데이터베이스는 다음과 같은 데이터를 최소한으로 관리할 수 있어야 합니다(추가 데이터를 관리해야 할 수도 있습니다).

다음은 데이터 모델의 대략적인 개요이며, 애플리케이션의 특별한 요구에 맞게 정제하고 수정해야 합니다.

- 사용자 데이터: 사용자 ID, 비밀번호 등
- 물건 데이터: 물건 카테고리 (카테고리 검색에 사용), 물건 설명 (키워드 검색에 사용), 상태 (새것, 거의 새것, 양호한 상태, 허용 가능한 상태), 판매자 ID, 즉시 구매 가격, 게시 날짜, 상태 등
- 입찰 데이터: 물건 ID, 입찰 가격, 입찰자, 게시 날짜, 입찰 마감 날짜 등
- 결제 데이터: 판매된 물건 ID, 구매 날짜, 판매자 ID, 구매자 ID, 구매자가 지불해야 할 금액, 판매자가 받아야 할 금액 등

참고: 데이터 모델은 BCNF 또는 3NF 중 하나를 만족시키기 위해 분해되어야 합니다. 귀하의 경매 회사는 다음과 같은 비즈니스 모델에 기반합니다: 판매자는 물건 가격의 10%에 해당하는 커미션 수수료를 지불해야 하며, 1 KRW 미만의 커미션은 내림 처리됩니다. 예를 들어, 물건 가격이 85 KRW인 경우, 판매자는 8 KRW을 경매 회사에 지불합니다. 입찰 기간이 종료되었을 때 (즉, 입찰 마감 시간이 지난 경우), 최고 입찰가를 결정하고 낙찰자에게 판매된 물건의 가격을 청구해야 합니다. PostgreSQL은 시간 기반 이벤트 스케줄러를 지원하지 않으므로 프로그램에서는 각 경매 물품마다 입찰 마감 시간이 지

났는지 여부를 지속적으로 확인하지 않아도 됩니다. 대신에 다음과 같은 시나리오에서 입찰 마감 시간을 확인해야 합니다.

1. 첫째, '물건 구매' 메뉴에서 물건을 검색하기 위해 select 쿼리를 수행할 때, 입찰 마감 날짜를 현재 시간과 비교하고, 입찰이 종료된 물건은 표시되지 않아야 합니다.
2. 둘째, 사용자가 물건에 입찰할 때, 업데이트 쿼리에서 현재 타임스탬프를 입찰 마감 날짜와 비교해야 합니다. 만약 입찰 마감 날짜가 이미 지났다면, 프로그램은 "입찰 종료"라는 오류 메시지를 출력해야 합니다.
3. 셋째, '계정 확인' 또는 '입찰 상태 확인' 메뉴가 선택될 때, 해당 함수들은 현재 판매 중인 물건들의 입찰 마감 날짜를 현재 시간과 비교하고, 입찰이 종료된 물건들에 대해 적절한 거래 처리를 수행해야 합니다.

이 프로젝트에서는 결제 시스템을 구현할 필요가 없습니다. 대신, 관리자는 각 사용자의 계정 잔액을 확인하여 사용자가 경매 회사에 얼마나 빚을 지고 있는지, 회사가 사용자(즉, 판매자)에게 얼마나 빚을 지고 있는지를 결정할 것입니다.

- 귀하의 프로그램은 구매자가 카테고리, 상태, 키워드, 판매자 및 날짜에 따라 경매 물품을 검색할 수 있어야 합니다. 검색 결과는 다음 정보를 포함한 잘 정리된 테이블에 표시되어야 합니다: 경매 종료 시간, 현재 최고 입찰가, 현재 최고 입찰자 및 즉시 구매 가격.

이 요구 사항은 조교가 출력 테이블을 쉽게 확인할 수 있도록하기 위해 설정된 것임을 주목해야 합니다. 테이블이 읽을 수 없으면, 조교들은 철저한 검토를 할 수 없을 수 있으며, 이로 인해 점수 감점이 발생할 수 있습니다.

- 판매자는 자신의 경매 물품 목록과 각 물품의 상태를 볼 수 있어야 합니다.
- 구매자도 자신이 입찰한 경매 물품 목록을 볼 수 있어야 합니다.
- 자세한 내용은 제공된 스켈레톤 코드를 확인하십시오.

# 3. 결과물

- E-R 모델을 사용한 스키마 다이어그램
- 구현한 기능 목록
- 구현하지 않은 기능 목록

소스 코드는 다음과 같은 내용으로 구성되어야 합니다.

- ddl.sql: 애플리케이션의 데이터베이스 스키마를 생성하는 DDL 문장
- data.sql: 일부 샘플 입력 레코드로 데이터베이스 테이블을 채우는 INSERT 문장
- 다른 모든 소스 코드, 이는 DML (INSERT/UPDATE/DELETE/SELECT 등) 문장을 포함합니다.
