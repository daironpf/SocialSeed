export const TypeOfModals = Object.freeze({
    WARNING: 'warning' as 'warning',
    INFORMATION: 'information' as 'information',
    ALERT: 'alert' as 'alert',
    ERROR: 'error' as 'error'
});

export type TypeOfModals = (typeof TypeOfModals)[keyof typeof TypeOfModals];
