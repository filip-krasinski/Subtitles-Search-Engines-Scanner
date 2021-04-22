export const download = (name: string, blob: Blob) => {
    const element = document.createElement('a');
    element.href = URL.createObjectURL(blob);
    element.download = name;
    document.body.appendChild(element);
    element.click();
}