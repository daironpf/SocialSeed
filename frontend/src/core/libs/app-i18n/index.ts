import {nextTick} from 'vue'
import {createI18n} from 'vue-i18n'

import localeEN from './messages/en.json';

export const DEFAULT_LOCALE = 'EN';

export const SUPPORT_LOCALES = ['EN', 'ES']

export function setI18nLanguage(i18n, locale) {
    i18n.global.locale.value = locale

    document.querySelector('html').setAttribute('lang', locale.toLowerCase())
}

export function setupI18n(options) {
    const i18n = createI18n(options)
    setI18nLanguage(i18n, options.locale)

    return i18n
}

export async function loadLocaleMessages(i18n, locale) {
    // load locale messages with dynamic import
    const messages = await import(`./messages/${locale.toLowerCase()}.json`)

    // set locale and locale message
    i18n.global.setLocaleMessage(locale, messages.default)

    return nextTick()
}

const i18n = setupI18n({
    locale: DEFAULT_LOCALE,
    fallbackLocale: DEFAULT_LOCALE,
    legacy: false,
    messages: {
        [DEFAULT_LOCALE]: localeEN
    }
});

export {i18n};