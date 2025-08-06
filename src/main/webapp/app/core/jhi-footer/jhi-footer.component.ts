import { defineComponent, ref } from 'vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'JhiFooter',
  setup() {
    const currentYear = ref<number>(new Date().getFullYear());
    return {
      currentYear,
      jhiFooter: {
        version: '1.0.0',
        website: 'https://hmvsoluciones.com',
      },
    };
  },
});
