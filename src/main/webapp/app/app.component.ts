import { defineComponent, provide, ref, inject } from 'vue';
import { storeToRefs } from 'pinia';

import { useLoginModal } from '@/account/login-modal';
import LoginForm from '@/account/login-form/login-form.vue';
import Ribbon from '@/core/ribbon/ribbon.vue';
import JhiFooter from '@/core/jhi-footer/jhi-footer.vue';
import JhiNavbar from '@/core/jhi-navbar/jhi-navbar.vue';
import { useAlertService } from '@/shared/alert/alert.service';
import '@/shared/config/dayjs';

import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'App',
  components: {
    ribbon: Ribbon,
    'jhi-navbar': JhiNavbar,
    'login-form': LoginForm,
    'jhi-footer': JhiFooter,
  },
  setup() {
    provide('alertService', useAlertService());

    const { loginModalOpen } = storeToRefs(useLoginModal());

    // Sidebar visible o no
    const sidebarVisible = ref(false);

    // Función para alternar sidebar
    function toggleSidebar() {
      sidebarVisible.value = !sidebarVisible.value;
    }

    // Obtener servicio account para roles
    const accountService = inject<AccountService>('accountService');

    // Cache para estados de roles
    const hasAnyAuthorityValues = ref<Record<string, boolean>>({});

    // Función para validar rol con caché y llamada async
    function hasRole(role: string): boolean {
      if (!accountService) return false;

      if (hasAnyAuthorityValues.value[role] !== undefined) {
        return hasAnyAuthorityValues.value[role];
      }

      accountService.hasAnyAuthorityAndCheckAuth([role]).then(result => {
        hasAnyAuthorityValues.value = {
          ...hasAnyAuthorityValues.value,
          [role]: result,
        };
      });

      return false; // default mientras carga
    }

    return {
      loginModalOpen,
      sidebarVisible,
      toggleSidebar,
      hasRole,
    };
  },
});
