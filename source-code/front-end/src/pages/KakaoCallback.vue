<template>
  <div>카카오 로그인 처리 중...</div>
</template>

<script>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/lib/api'

export default {
  setup() {
    const router = useRouter()

    onMounted(async () => {
      const code = new URLSearchParams(window.location.search).get('code')
      if (!code) {
        alert('카카오 로그인 실패: 인가 코드 없음')
        return
      }

      try {
        const res = await api.post('/auth/kakao', { code })
        const token = res.data.token
        localStorage.setItem('token', token)
        router.push('/home')
      } catch (err) {
        alert('카카오 로그인 처리 중 오류 발생')
        console.error(err)
      }
    })
  },
}
</script>
