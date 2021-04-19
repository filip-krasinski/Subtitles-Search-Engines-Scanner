import { isoLangs } from "../languages";
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
                isoLangs.map(language =>
                    <option key={language.code} value={language.code}>
                        {language.nativeName}
                    </option>
                )
            }
            </select>
        </div>
    )
}