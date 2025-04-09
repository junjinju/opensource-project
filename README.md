# **PROJECT PROPOSAL**

| Item          | Details                                                                 |
|---------------|-------------------------------------------------------------------------|
| **Course**        | Open Source Software Basic                          |
| **Class**        | 3 |
| **Submitted on**  | April 1, 2025                                                           |
| **Owner**         | 하경준 |
| **Members**       | 곽태훈, 한승진, 이예권 |

<br>

# **CONTENTS**

[**1. TEAM**](#1-team)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**1.1. MEMBERS**](#11-members)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**1.2. TOOLS FOR COOPERATION**](#12-tools-for-cooperation)  <br>

[**2. PROJECT INFO.**](#2-project-info)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**2.1. PROJECT NAME**](#21-project-name)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**2.2. PROJECT ICON**](#22-project-icon)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**2.3. PROJECT OVERVIEW**](#23-project-overview)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[**2.3.1 BACKGROUND**](#231-background)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[**2.3.2 SERVICE TARGET**](#232-service-target)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[**2.3.3 BENEFITS**](#233-benefits)  <br>

[**3. REQUIREMENTS SPECIFICATION**](#3-requirements-specification)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**3.1. KEY FEATURES**](#31-key-features)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[**3.1.1. PURPOSE AND IMPLEMENTATION**](#311-purpose-and-implementation)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[**3.1.2. INPUT/OUTPUT AND EXCEPTION HANDLING**](#312-inputoutput-and-exception-handling)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**3.2. EXTENSION**](#32-extension)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[**3.2.1. PURPOSE AND IMPLEMENTATION**](#321-purpose-and-implementation)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[**3.2.2. INPUT/OUTPUT AND EXCEPTION HANDLING**](#322-inputoutput-and-exception-handling)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**3.3. ERD (Entity-Relationship Diagram)**](#33-erd-entity-relationship-diagram)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**3.4. UX/UI (WIREFRAME)**](#34-uxui-wireframe)  <br>

[**4. DEVELOPMENT STRATEGY**](#4-development-strategy)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**4.1. DEVELOPMENT METHODOLOGY & TASK ALLOCATION**](#41-development-methodology--task-allocation)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**4.2. SCHEDULE (MILESTONE)**](#42-schedule-milestone)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**4.3. TESTING PLAN**](#43-testing-plan)  <br>

[**5. RISK ASSESSMENT & MITIGATION**](#5-risk-assessment--mitigation)  <br>

[**6. REFERENCES & TERMS**](#6-references--terms)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**6.1 REFERENCES**](#61-references)  <br>
&nbsp;&nbsp;&nbsp;&nbsp;[**6.2 TERMS**](#62-terms)  <br>

<br>

# **1. TEAM**

## **1.1. MEMBERS**

| **Name**   | **Position**                           |
|------------|----------------------------------------|
| 곽태훈     | UX/UI Design & Front-end (FE)         |
| 이예권     | UX/UI Design & Front-end (FE)         |
| 하경준     | PM & Back-end (BE)                    |
| 한승진     | Database Design & Back-end (BE)       |

## **1.2. TOOLS FOR COOPERATION**

| **Tool**   | **Purpose**                                                  |
|------------|---------------------------------------------------------------|
| FIGMA     | Sharing UX/UI designs and wireframes                          |
| GITHUB    | Sharing the entire project source code and version control    |

<br>

# **2. PROJECT INFO.**

## **2.1. PROJECT NAME**

<p align="center">
  "싹싹정리" <br> 
기분이 좋거나 일이 잘 풀렸을 때 사용하는 유행어인 “싹싹김치”에서 착안하여, 냉장고 재고를 ‘싹싹’ 정리하고 관리한다는 의미
</p>

## **2.2. PROJECT ICON**

<p align="center">
  <img src="media/image1.png">
</p>

## **2.3. PROJECT OVERVIEW**

### **2.3.1 BACKGROUND**

&nbsp;현대 사회에서는 **식재료 낭비와 가정 내 음식물 쓰레기**가 중요한 사회적 문제로 인식되고 있다. 특히 냉장고 속에 어떤 재료가 있는지 잊고, 유통기한이 지난 식재료를 폐기하는 일이 흔하며 이는 가계의 불필요한 지출로 이어지게 된다. 서울특별시에 따르면, 한국 가정에서 발생하는 음식물 쓰레기 중 약 **34%**가 유통기한 경과 식재료에서 발생하며, **연간 약 1조 원 이상의 음식물 쓰레기 처리 비용이 지출**되고 있다. 또한 식재료를 직접 관리하지 않고 방치하는 것은 **탄소배출 증가**와 **자원 낭비**로 이어져, 결국 환경 문제에도 영향을 미치게 된다.

&nbsp;기존의 재고 관리 앱은 대부분 **식재료를 수동으로 등록**해야 하며, 이 과정이 번거로워 장기 사용이 어렵다는 피드백이 많다. 이를 해결하기 위해 본 프로젝트에서는 **OCR (Optical Character Recognition)** 기술을 활용하여 **영수증 사진 한 장으로 식재료 정보를 자동으로 인식·입력**하는 방식을 도입할 예정이다. 또한, 단순 재고 관리에 그치지 않고, **보유 식재료 기반의 레시피 추천(GPT API)** 기능을 제공하여 일상에서 실질적으로 도움이 되는 서비스를 통합적으로 제공할 계획이다.

<p align="center">
  <img src="media/image2.png" alt="음식물 쓰레기 발생 원인 비율" width="500"><br>
  <em>Figure 1: 음식물 쓰레기 발생 원인 비율, 서울특별시</em>
</p>

<p align="center">
  <img src="media/image3.png" alt="음식물 쓰레기 발생량과 처리 비용 추이" width="500"><br>
  <em>Figure 2: 음식물 쓰레기 발생량과 처리 비용 추이 (2018–2023), 환경부·통계청 자료</em>
</p>


# **6. REFERENCES & TERMS**

## **6.1 REFERENCES**

\[1\] 서울특별시. (n.d.). *서울시 음식물류 폐기물 발생량 및 처리현황
통계.* 서울 열린데이터광장. 

\[2\] 환경부, & 한국환경공단. (2023). *전국 폐기물 발생 및 처리 현황.*

\[3\] 오순도순. (2023, November 30). *\[OCR/AI\] 2023년 최신판 OCR 8가지
API 비교평가 테스트.* DevOcean.
https://devocean.sk.com/blog/techBoardDetail.do?ID=165524&boardType=techBlog

\[4\] OpenAI. (n.d.). *Text generation and prompting*. OpenAI Platform.
https://platform.openai.com/docs/guides/text?api-mode=chat

## **6.2 TERMS**

**OCR (Optical Character Recognition)**

- 이미지 속 문자 영역을 인식하여 디지털 텍스트로 변환하는 기술

- 본 프로젝트에서는 영수증 이미지를 분석하여 구매 항목, 수량, 구매일자
  등을 추출하기 위해 사용

**MVP (Minimum Viable Product)**

- 최소한의 기능만으로 서비스의 핵심 가치를 제공할 수 있는 초기 제품을
  의미

- 본 프로젝트에서는 OCR 기반 재고 등록, 재고 확인, 기본 계정 기능이
  포함된 버전이 해당
