import { toast, type ToastOptions } from 'vue3-toastify'

export const toasts = {
  success(message: string, options?: ToastOptions): void {
    toast(message, {
      autoClose: 1000,
      position: toast.POSITION.BOTTOM_RIGHT,
      type: toast.TYPE.SUCCESS,
      theme: 'dark',
    } as ToastOptions)
  },
  error(message: string, options?: ToastOptions): void {
    toast(message, {
      autoClose: 1000,
      position: toast.POSITION.BOTTOM_RIGHT,
      type: toast.TYPE.ERROR,
      theme: 'dark',
    } as ToastOptions)
  },
  info(message: string, options?: ToastOptions): void {
    toast(message, {
      autoClose: 1000,
      position: toast.POSITION.BOTTOM_RIGHT,
      type: toast.TYPE.INFO,
      theme: 'dark',
    } as ToastOptions)
  },
}
