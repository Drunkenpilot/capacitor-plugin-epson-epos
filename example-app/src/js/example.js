import { EpsonEpos } from 'capacitor-plugin-epson-epos';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    EpsonEpos.echo({ value: inputValue })
}
