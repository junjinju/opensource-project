<template>
  <div class="login-page">
    <!-- 왼쪽 패널: 이미지 + 설명 -->
    <div class="left-panel">
      <img src="@/assets/fridge.png" alt="Fridge Illustration" />
      <h2>Manage Your Fridge Smarter</h2>
      <p>
        Track your groceries, reduce food waste, and discover recipes – all in one place. Our
        AI-powered system makes food management effortless.
      </p>
    </div>

    <!-- 오른쪽 패널: 로그인 폼 -->
    <div class="right-panel">
      <img src="@/assets/logo.png" alt="Logo" class="logo" />
      <div class="login-header">
        <h1>Welcome back</h1>
        <p class="signup">
          Don’t have an account?
          <a href="#" @click.prevent="showSignup = true">Sign up</a>
        </p>
      </div>

      <form v-if="!showSignup" @submit.prevent="login">
        <input v-model="email" type="email" placeholder="Email address" required />
        <input v-model="password" type="password" placeholder="Password" required />

        <div class="options">
          <label>
            Remember me
            <input type="checkbox" v-model="remember" />
          </label>
          <a href="#">Forgot password?</a>
        </div>

        <button type="submit" class="signin-btn">Sign in</button>
      </form>

      <form v-else @submit.prevent="signup">
        <input v-model="signupEmail" type="email" placeholder="Email address" required />
        <input v-model="signupPassword" type="password" placeholder="Password" required />
        <input v-model="signupNickname" type="text" placeholder="Nickname" required />
        <button type="submit" class="signin-btn">Create account</button>
        <p class="signup" style="text-align: center; margin-top: 1rem">
          Already have an account?
          <a href="#" @click.prevent="showSignup = false">Back to login</a>
        </p>
      </form>

      <div class="divider"><span>Or continue with</span></div>
      <button @click="kakaoLogin" class="kakao-btn">
        <img src="@/assets/kakao-icon.png" alt="Kakao icon" class="kakao-icon" />
        Sign in with KakaoTalk
      </button>
    </div>
  </div>
</template>

<script>
import api from '/src/api/index'

export default {
  name: 'LoginPage',
  data() {
    return {
      email: '',
      password: '',
      remember: false,
      showSignup: false,
      signupEmail: '',
      signupPassword: '',
      signupNickname: '',
    }
  },
  methods: {
    kakaoLogin() {
      const REST_API_KEY = '7bab93bebb2263ccc8ddcd817fca9a8f'
      const REDIRECT_URI = 'http://localhost:5173/oauth/kakao/callback'
      const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`
      window.location.href = kakaoAuthUrl
    },
    async login() {
      try {
        const response = await api.post(
          '/auth/login',
          {
            email: this.email,
            password: this.password,
          },
          {
            headers: { 'Content-Type': 'application/json' },
          },
        )
        const token = response.data.accessToken || response.data.token
        localStorage.setItem('token', token)
        console.log(token)
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`
        this.$router.push('/home')
      } catch (err) {
        console.error(err)
        alert('로그인 실패. 이메일 또는 비밀번호를 확인하세요.')
      }
    },
    async signup() {
      try {
        await api.post(
          '/auth/signup',
          {
            email: this.signupEmail,
            password: this.signupPassword,
            nickname: this.signupNickname,
          },
          {
            headers: { 'Content-Type': 'application/json' },
          },
        )
        alert('회원가입 성공! 로그인해주세요.')
        this.showSignup = false
        this.email = this.signupEmail
        this.password = this.signupPassword
      } catch (err) {
        console.error(err)
        alert('회원가입 실패. 이메일이 이미 존재하거나 형식이 잘못되었습니다.')
      }
    },
  },
}
</script>

<style scoped>
.login-page {
  display: flex;
  height: 100vh;
  font-family: Arial, sans-serif;
}

.left-panel {
  flex: 1;
  background-color: #000;
  color: #fff;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.left-panel img {
  width: 200px;
  margin-bottom: 1rem;
}
.left-panel h2 {
  font-size: 1.5rem;
  margin-top: 1rem;
}
.left-panel p {
  margin-top: 1rem;
  line-height: 1.5;
  text-align: center;
}

.right-panel {
  flex: 1;
  padding: 3rem 4rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.logo {
  height: 60px;
  width: auto;
  display: block;
  margin: 0 auto 1rem auto;
  object-fit: contain;
}
.right-panel h1 {
  margin: 0.5rem 0;
}
.login-header {
  text-align: center;
  margin-bottom: 2rem;
}
.signup {
  margin-bottom: 2rem;
  font-size: 0.9rem;
}
.signup a {
  color: #3498db;
  text-decoration: none;
}

form input {
  width: 100%;
  padding: 0.75rem;
  margin-bottom: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.options label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
}

.options a {
  color: #3498db;
  text-decoration: none;
}

.signin-btn {
  width: 100%;
  padding: 0.75rem;
  background-color: #000;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.divider {
  text-align: center;
  margin: 1.5rem 0;
  position: relative;
}
.divider::before,
.divider::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 40%;
  height: 1px;
  background-color: #ccc;
}
.divider::before {
  left: 0;
}
.divider::after {
  right: 0;
}
.divider span {
  background-color: #fff;
  padding: 0 0.5rem;
  color: #888;
}

.kakao-btn {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ccc;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0.5rem 1rem;
  background-color: #fee500;
  border: none;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
}

.kakao-icon {
  width: 20px;
  height: 20px;
}
</style>
