<template>
  <div class="account-page">
    <h1>{{ $t('account.title') }}</h1>
    <p class="subtitle">{{ $t('account.subtitle') }}</p>

    <div class="account-container">
      <!-- 개인 정보 -->
      <section class="panel personal-info">
        <h2>{{ $t('account.personalInfo') }}</h2>
        <div class="fields">
          <div class="field-group">
            <label for="name">{{ $t('account.name') }}</label>
            <input id="name" type="text" v-model="user.name" />
          </div>
          <div class="field-group">
            <label for="email">{{ $t('account.email') }}</label>
            <input id="email" type="email" v-model="user.email" disabled />
          </div>
          <div class="field-group">
            <label for="phone">{{ $t('account.phone') }}</label>
            <input id="phone" type="text" v-model="user.phone" />
          </div>
          <div class="field-group">
            <label for="lang">{{ $t('account.language') }}</label>
            <select v-model="user.language">
              <option value="en">{{ $t('account.english') }}</option>
              <option value="ko">{{ $t('account.korean') }}</option>
            </select>
          </div>
        </div>
      </section>

      <!-- 계정 연동 -->
      <section class="panel connect-accounts">
        <h2>{{ $t('account.connectAccounts') }}</h2>
        <button class="connect-btn">
          <img src="@/assets/google-icon.png" alt="Google" /> Connect with Google
        </button>
        <button class="connect-btn">
          <img src="@/assets/facebook-icon.png" alt="Facebook" /> Connect with Facebook
        </button>
        <button class="connect-btn">
          <img src="@/assets/apple-icon.png" alt="Apple" /> Connect with Apple
        </button>
      </section>

      <!-- 보안 설정 -->
      <section class="panel security">
        <h2>{{ $t('account.security') }}</h2>
        <ul class="sec-list">
          <li @click="showPasswordModal = true">Change Password ➔</li>
          <li @click="setup2FA">Two-Factor Authentication ➔</li>
        </ul>
      </section>

      <!-- 알림 설정 -->
      <section class="panel preferences">
        <h2>{{ $t('account.preferences') }}</h2>
        <div class="preference-option">
          <label>
            <input type="checkbox" v-model="prefs.emailNotifs" />
            <span>{{ $t('account.emailNotifs') }}</span>
          </label>
        </div>
        <div class="preference-option">
          <label>
            <input type="checkbox" v-model="prefs.smsNotifs" />
            <span>{{ $t('account.smsNotifs') }}</span>
          </label>
        </div>
      </section>
    </div>

    <!-- 저장/취소 버튼 -->
    <div class="actions">
      <button class="btn save" @click="saveAll">Save Changes</button>
      <button class="btn cancel" @click="resetAll">Cancel</button>
    </div>

    <!-- 비밀번호 변경 모달 -->
    <div v-if="showPasswordModal" class="modal">
      <div class="modal-box">
        <h3>Change Password</h3>
        <div class="field-group">
          <label>New Password</label>
          <input type="password" v-model="passwordForm.newPassword" />
        </div>
        <div class="field-group">
          <label>Confirm Password</label>
          <input type="password" v-model="passwordForm.confirmPassword" />
        </div>
        <div class="modal-actions">
          <button @click="submitPasswordChange">Submit</button>
          <button @click="showPasswordModal = false">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { updatePassword } from '@/api/user'

export default {
  name: 'AccountPage',
  data() {
    return {
      user: {
        name: '',
        email: '',
        phone: '',
        language: 'en',
      },
      prefs: {
        emailNotifs: true,
        smsNotifs: false,
      },
      showPasswordModal: false,
      passwordForm: {
        newPassword: '',
        confirmPassword: '',
      },
    }
  },
  methods: {
    setup2FA() {
      alert('Navigate to two-factor setup')
    },
    saveAll() {
      this.$i18n.locale = this.user.language
      alert('변경사항이 저장되었습니다.')
    },
    resetAll() {
      this.user = { name: '', email: '', phone: '', language: 'en' }
      this.prefs = { emailNotifs: true, smsNotifs: false }
    },
    async submitPasswordChange() {
      const { newPassword, confirmPassword } = this.passwordForm
      if (!newPassword || newPassword !== confirmPassword) {
        alert('Passwords do not match.')
        return
      }
      try {
        await updatePassword(1, newPassword) // 사용자 ID는 실제 로직에 맞게 조정
        alert('Password updated successfully.')
        this.showPasswordModal = false
        this.passwordForm = { newPassword: '', confirmPassword: '' }
      } catch (err) {
        console.error('Password update failed:', err)
        alert('Error updating password.')
      }
    },
  },
  watch: {
    'user.language'(newLang) {
      this.$i18n.locale = newLang
    },
  },
}
</script>

<style scoped>
.account-page {
  padding: 2rem;
  font-family: Arial, sans-serif;
}
.subtitle {
  color: #666;
  margin-bottom: 1.5rem;
}
.account-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-template-rows: repeat(2, auto);
  gap: 1.5rem;
  margin-bottom: 2rem;
}
.panel {
  background: #fff;
  padding: 2rem;
  min-height: 250px;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}
.personal-info .fields {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem 2rem;
  margin-top: 1rem;
}
.field-group {
  display: flex;
  flex-direction: column;
}
.field-group label {
  font-weight: 500;
  margin-bottom: 0.4rem;
  font-size: 0.9rem;
}
.field-group input,
.field-group select {
  padding: 0.6rem 0.75rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 0.9rem;
}
.preference-option {
  margin-bottom: 0.75rem;
}
.connect-accounts .connect-btn {
  display: flex;
  align-items: center;
  padding: 0.5rem;
  margin-bottom: 0.5rem;
  background: #f5f5f5;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
}
.connect-accounts .connect-btn img {
  width: 20px;
  margin-right: 0.5rem;
}
.security .sec-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.security .sec-list li {
  padding: 0.5rem 0;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}
.actions {
  text-align: right;
}
.btn {
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.btn.save {
  background: #000;
  color: #fff;
  margin-right: 0.5rem;
}
.btn.cancel {
  background: #777;
  color: #fff;
}

/* 모달 스타일 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-box {
  background: white;
  padding: 2rem;
  border-radius: 10px;
  width: 400px;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
  margin-top: 1rem;
}
</style>
