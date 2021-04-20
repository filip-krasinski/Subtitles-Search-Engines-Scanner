import { isoLangs } from "../languages.json";
import React, {ChangeEvent, useEffect, useState} from "react";

interface IProps {
    onSelect: (language: string) => void
}

export const DropdownLanguage: React.FC<IProps> = ({ onSelect }) => {
    const [defaultLang,  setLang]  = useState<string>("en")

    const onChange = (e: ChangeEvent<HTMLSelectElement> ) => {
        setLang(e.target.value)
        onSelect(e.target.value)
    }

    useEffect(() => {
        // @ts-ignore
        const language = window.navigator.userLanguage || window.navigator.language;
        const found = isoLangs.find((el) => language.startsWith(el.code))?.code
        if (found !== undefined) {
            setLang(found)
            onSelect(found)
        }
    }, [])

    return (
        <div className="select-btn-streaming">
            <select
                value={defaultLang}
                onChange={(e) => onChange(e)}
            >
            {
                isoLangs.map(lang =>
                    <option key={lang.code} value={lang.code}>
                        {lang.nativeName}
                    </option>
                )
            }
            </select>
        </div>
    )
}